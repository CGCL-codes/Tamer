package com.linuxandbeer.hop2it.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import com.linuxandbeer.hop2it.R;
import com.linuxandbeer.hop2it.db.IngredientDbAdapter;
import com.linuxandbeer.hop2it.ingredients.Fermentable;

/**
 * Basic editor for adding or modifying grains in the ingredient Db.
 */
public class GrainDbEditor extends Activity {

    private IngredientDbAdapter mDbHelper;

    private int index;

    private EditText mShortName;

    private EditText mLongName;

    private EditText mDescription;

    private EditText mExtract;

    private EditText mColor;

    /**
	 * Called when the activity is created
	 * @param savedInstanceState
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new IngredientDbAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.grain_db_editor);
        index = getIntent().getIntExtra(IngredientDbAdapter.KEY_ID, -1);
        mShortName = (EditText) findViewById(R.id.ShortNameEditText);
        mLongName = (EditText) findViewById(R.id.LongNameEditText);
        mDescription = (EditText) findViewById(R.id.DescEditText);
        mExtract = (EditText) findViewById(R.id.ExtEditText);
        mColor = (EditText) findViewById(R.id.ColEditText);
        if (index != -1) {
            Fermentable f = mDbHelper.fetchGrain(index);
            mLongName.setText(f.name);
            mShortName.setText(f.manufacturer);
            mDescription.setText(f.description);
            mExtract.setText(Double.toString(f.extract));
            mColor.setText(Double.toString(f.color));
        }
    }

    /**
	 * Called when user presses the Menu button for the first time in this activity
	 * @param menu
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 0, Menu.NONE, "Save").setIcon(android.R.drawable.ic_menu_save);
        menu.add(0, 1, Menu.NONE, "Cancel").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }

    /**
	 * Called when a user selects a menu option
	 * @param item
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case 0:
                {
                    if (index == -1) {
                        mDbHelper.createGrain(mLongName.getText().toString(), mShortName.getText().toString(), mDescription.getText().toString(), Float.parseFloat(mExtract.getText().toString()), Float.parseFloat(mColor.getText().toString()));
                    } else {
                        mDbHelper.updateGrain(index, mLongName.getText().toString(), mShortName.getText().toString(), mDescription.getText().toString(), Float.parseFloat(mExtract.getText().toString()), Float.parseFloat(mColor.getText().toString()));
                    }
                    mDbHelper.close();
                    finish();
                    return true;
                }
            case 1:
                {
                    mDbHelper.close();
                    finish();
                }
        }
        return true;
    }
}
