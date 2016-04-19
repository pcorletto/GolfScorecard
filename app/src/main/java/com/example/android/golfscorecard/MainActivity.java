package com.example.android.golfscorecard;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ListActivity {

    private static final String PREFS_FILE = "com.example.android.golfscorecard.preferences";
    private static final String KEY_STROKECOUNT = "key_strokecount";
    private Hole[] mHoles = new Hole[18];
    private ListAdapter mListAdapter;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);

        mEditor = mSharedPreferences.edit();

        // Initialize holes
        int strokes = 0;

        for(int i=0; i<mHoles.length; i++){

            mHoles[i] = new Hole("Hole " + ( i + 1) + " :", strokes);

            strokes = mSharedPreferences.getInt(KEY_STROKECOUNT + i, 0);

        }

        mListAdapter = new ListAdapter(this, mHoles);
        setListAdapter(mListAdapter);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the score for each hole to a different shared preference.

        for(int i=0; i<mHoles.length; i++){

            mEditor.putInt(KEY_STROKECOUNT + i, mHoles[i].getStrokeCount());

        }

        mEditor.apply();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_strokes) {

            // Reset stroke counts to zero for all holes.

            mEditor.clear();
            mEditor.apply();

            // Set each of the hole scores = 0.

            for(Hole hole: mHoles){
                hole.setStrokeCount(0);
            }

            mListAdapter.notifyDataSetChanged();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
