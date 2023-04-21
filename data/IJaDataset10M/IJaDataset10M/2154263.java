package edu.uiuc.android.scorch.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.uiuc.android.scorch.GameState;
import edu.uiuc.android.scorch.R;

/**
 * The main menu activity for the game, Scorched Android
 */
public class MainMenuActivity extends ListActivity {

    private static String[] sMainMenuItems = { "New Game", "Restore Game" };

    private static final int RC_NEW_GAME = 1;

    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState A previous game state which should be restored, or null. This could
     *        happen if the game was paused or interrupted, such as by a phone call.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("ScorchedAndroid", "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sMainMenuItems);
        setListAdapter(adapter);
    }

    /**
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case RC_NEW_GAME:
                GameState gameState = null;
                if (data != null && (gameState = (GameState) data.getSerializableExtra("GameState")) != null) {
                    Intent i = new Intent();
                    i.setClass(MainMenuActivity.this, GameActivity.class);
                    i.putExtra("GameState", gameState);
                    startActivity(i);
                }
                break;
        }
    }

    /**
     * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View,
     *      int, long)
     */
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        String item = (String) getListView().getItemAtPosition(position);
        if (item == "New Game") {
            Intent i = new Intent();
            i.setClass(MainMenuActivity.this, SetupActivity.class);
            startActivityForResult(i, RC_NEW_GAME);
        } else if (item == "Restore Game") {
            Intent i = new Intent();
            i.setClass(MainMenuActivity.this, GameActivity.class);
            i.putExtra("restore", true);
            startActivityForResult(i, 0);
        } else if (item == "Options") {
        }
    }
}
