package com.example.flixjane;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.MovieAdapter;
import models.Movie;
import okhttp3.Headers;


public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=24f58dcbd96a71b4da1a96fa2d8f3979";
    public static final String TAG = "MainActivity";
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        //Create adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        //set it on the recycler view
        rvMovies.setAdapter(movieAdapter);

        //set layout manager on recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                 Log.d(TAG, "onSuccess");
                 JSONObject jsonObject = json.jsonObject;
                 try{
                     JSONArray results = jsonObject.getJSONArray("results");
                     Log.i(TAG, "Results "+ results.toString());
                     movies.addAll(Movie.fromJsonArray(results));
                     movieAdapter.notifyDataSetChanged();
                     Log.i(TAG, "Movies "+ movies.size());

                 }
                 catch (JSONException e){
                     Log.e(TAG, "Hit json exception", e);
                 }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}