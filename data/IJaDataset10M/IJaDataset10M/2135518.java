package com.burgstaller.aGTD;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.burgstaller.controls.IconifiedText;
import com.burgstaller.controls.IconifiedTextListAdapter;
import com.burgstaller.provider.AGtd;

/**
 * @author rainer
 *
 */
public class MainMenu extends ListActivity {

    private static final int SETTINGS_ID = Menu.FIRST;

    public static final int CREATE_NEW_TASK = 0;

    public static final int CONTEXTS = CREATE_NEW_TASK + 2;

    public static final int PROJECTS = CONTEXTS + 1;

    public static final int INBOX = PROJECTS + 1;

    public static final int OVERDUE = INBOX + 2;

    public static final int SYNC = OVERDUE + 2;

    public static final int DELETE_COMPLETED = SYNC + 1;

    String[] m_options = { "Contexts", "Projects", "Process / Review" };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        IconifiedTextListAdapter adapter = new IconifiedTextListAdapter(this);
        adapter.addItem(new IconifiedText(getText(R.string.main_create_task), getResources().getDrawable(R.drawable.newtask)));
        adapter.addItem(new IconifiedText());
        adapter.addItem(new IconifiedText(getText(R.string.main_contexts), getResources().getDrawable(R.drawable.context)));
        adapter.addItem(new IconifiedText(getText(R.string.main_projects), getResources().getDrawable(R.drawable.project)));
        adapter.addItem(new IconifiedText(getText(R.string.main_inbox), getResources().getDrawable(R.drawable.inbox)));
        adapter.addItem(new IconifiedText());
        adapter.addItem(new IconifiedText(getText(R.string.main_overdue), getResources().getDrawable(R.drawable.overdue)));
        adapter.addItem(new IconifiedText());
        adapter.addItem(new IconifiedText(getText(R.string.main_sync), getResources().getDrawable(R.drawable.sync)));
        adapter.addItem(new IconifiedText(getText(R.string.main_delete_completed), getResources().getDrawable(R.drawable.delete_tasks)));
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, SETTINGS_ID, R.string.header_settings);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, Item item) {
        switch(item.getId()) {
            case SETTINGS_ID:
                openSettings();
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    private void openSettings() {
        Intent i = new Intent(this, SettingsEditor.class);
        startActivity(i);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent targetIntent;
        Log.d(AGtd.LOG_TAG, "listitemclick: " + id);
        switch(position) {
            case CONTEXTS:
                Intent intent = getIntent();
                Uri data = intent.getData();
                targetIntent = new Intent(Intent.VIEW_ACTION, AGtd.Contexts.CONTENT_URI);
                startActivity(targetIntent);
                break;
            case PROJECTS:
                targetIntent = new Intent(Intent.VIEW_ACTION, AGtd.Projects.CONTENT_URI);
                startActivity(targetIntent);
                break;
            case OVERDUE:
                targetIntent = new Intent(Intent.VIEW_ACTION, AGtd.Tasks.CONTENT_URI);
                String filter = AGtd.Tasks.DUE_DATE + " < " + System.currentTimeMillis() + " AND " + AGtd.Tasks.DUE_DATE + " > 0";
                Log.d(AGtd.LOG_TAG, "filter: " + filter);
                targetIntent.putExtra("filter", filter);
                startActivity(targetIntent);
                break;
            case SYNC:
                startSync();
                break;
            case DELETE_COMPLETED:
                Cursor toDelete = managedQuery(AGtd.Tasks.CONTENT_URI, null, AGtd.Tasks.COMPLETED + "=1 AND " + AGtd.Tasks.SYNC_STATUS + " != ?", new String[] { "" + AGtd.Tasks.SYNC_STATUS_DELETED }, null);
                int numRecords = toDelete.count();
                AGtdContentProvider.popCursor(toDelete);
                if (numRecords > 0) {
                    showAlert("Are you sure", -1, "Are you sure to delete " + numRecords + " tasks?", "Delete", new OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            deleteCompleted();
                        }
                    }, true, null);
                } else {
                    Toast.makeText(this, R.string.nothing_to_delete, Toast.LENGTH_SHORT).show();
                }
                break;
            case INBOX:
                startActivity(new Intent(Intent.VIEW_ACTION, Uri.withAppendedPath(AGtd.Tasks.CONTENT_URI_FOR_CONTEXT, "" + AGtd.Contexts.INBOX_RECORD_ID)));
                break;
            case CREATE_NEW_TASK:
                startActivity(new Intent(Intent.INSERT_ACTION, AGtd.Tasks.CONTENT_URI));
                break;
        }
    }

    private void deleteCompleted() {
        ContentValues values = new ContentValues();
        values.put(AGtd.Tasks.SYNC_STATUS, AGtd.Tasks.SYNC_STATUS_DELETED);
        values.put(AGtd.Tasks.MODIFIED_DATE, System.currentTimeMillis());
        getContentResolver().update(AGtd.Tasks.CONTENT_URI, values, AGtd.Tasks.COMPLETED + " = 1", null);
    }

    private void startSync() {
        startActivity(new Intent(this, SyncView.class));
    }
}
