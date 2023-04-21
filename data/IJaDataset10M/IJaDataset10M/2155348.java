package net.vadima.android.mysqladmin;

import net.vadima.android.mysqladmin.database.DbAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ServersOverview extends ListActivity {

    private DbAdapter dbHelper;

    private static final int ACTIVITY_CREATE = 0;

    private static final int ACTIVITY_EDIT = 1;

    private static final int DELETE_ID = Menu.FIRST + 1;

    private static final int EDIT_ID = Menu.FIRST + 2;

    private static final String TAG = "ServersOverview";

    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_list);
        this.getListView().setDividerHeight(2);
        dbHelper = new DbAdapter(this);
        dbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_server_add:
                addServer();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_server_add:
                addServer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                dbHelper.deleteServer(info.id);
                Log.d(TAG, "Deleted ID: " + info.id);
                fillData();
                return true;
            case EDIT_ID:
                AdapterContextMenuInfo info_edit = (AdapterContextMenuInfo) item.getMenuInfo();
                Intent i = new Intent(this, ServerDetails.class);
                i.putExtra(DbAdapter.KEY_ROWID, info_edit.id);
                startActivityForResult(i, ACTIVITY_EDIT);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void addServer() {
        Intent i = new Intent(this, ServerDetails.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d(TAG, "Clicked ID: " + id);
        Intent i = new Intent(this, ServerDatabases.class);
        i.putExtra(DbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

    private void fillData() {
        cursor = dbHelper.fetchAllServers();
        startManagingCursor(cursor);
        String[] from = new String[] { DbAdapter.KEY_TITLE, DbAdapter.KEY_HOSTNAME };
        int[] to = new int[] { R.id.tv_title, R.id.tv_hostname };
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.server_row, cursor, from, to);
        setListAdapter(notes);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        menu.add(0, EDIT_ID, 0, R.string.menu_edit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
