package com.rgm.gasbook;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.rgm.gasbook.R;
import com.rgm.gasbook.entity.Vehicle.VehicleColumns;

public class VehiclesList extends ListActivity {

    private static final String TAG = "VehiclesList";

    /**
	 * The columns we are interested in from the database
	 */
    private static final String[] PROJECTION = new String[] { VehicleColumns._ID, VehicleColumns.NAME };

    /** The index of the title column */
    private static final int COLUMN_INDEX_TITLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(VehicleColumns.CONTENT_URI);
        }
        getListView().setOnCreateContextMenuListener(this);
        Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null, VehicleColumns.DEFAULT_SORT_ORDER);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.vehicleslist_item, cursor, new String[] { VehicleColumns.NAME }, new int[] { android.R.id.text1 });
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_options_menu, menu);
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, new ComponentName(this, VehiclesList.class), null, intent, 0, null);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            Log.e(TAG, "bad menuInfo", e);
            return;
        }
        Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
        if (cursor == null) {
            return;
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
        menu.setHeaderTitle(cursor.getString(COLUMN_INDEX_TITLE));
        Intent intent = new Intent(null, Uri.withAppendedPath(getIntent().getData(), Integer.toString((int) info.id)));
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, new ComponentName(this, VehiclesList.class), null, intent, 0, null);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e(TAG, "bad menuInfo", e);
            return false;
        }
        Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), info.id);
        switch(item.getItemId()) {
            case R.id.context_open:
                startActivity(new Intent(Intent.ACTION_EDIT, noteUri));
                return true;
            case R.id.context_delete:
                getContentResolver().delete(noteUri, null, null);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), id);
        String action = getIntent().getAction();
        if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
            setResult(RESULT_OK, new Intent().setData(noteUri));
        } else {
            startActivity(new Intent(Intent.ACTION_EDIT, noteUri));
        }
    }
}
