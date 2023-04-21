package net.learn2develop.AndroidViews;

import net.learn2develop.R;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.view.View;
import android.app.ListActivity;
import static android.provider.BaseColumns._ID;

public class DbContactConsole extends ListActivity {

    private static String[] FROM = { _ID, DbConstants.NAME, DbConstants.PHONE, DbConstants.EMAIL };

    private DbCreate contacts;

    private static SQLiteDatabase db;

    private static int[] TO = { R.id.rowid, R.id.name, R.id.mobilephone, R.id.email };

    private ListView lv1;

    private static String itemId;

    private Cursor cursor;

    static final int CONTACT_CANCELED = 0;

    static final int CONTACT_ADDED = 1;

    static final int CONTACT_MODIFIED = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlist);
        showDatabaseContent();
        lv1 = getListView();
        lv1.setTextFilterEnabled(true);
        lv1.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                cursor = (Cursor) a.getItemAtPosition(position);
                itemId = cursor.getString(0);
                openOptionsMenu();
            }
        });
        lv1.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void showDialogItemId(long itemId) {
        Toast.makeText(this, "Menu item selected index is" + Long.toString(itemId), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.modifyitem:
                if (null != itemId) {
                    Bundle contactToModify = new Bundle();
                    contactToModify.putString("cFirstName", cursor.getString(1));
                    contactToModify.putString("cMobilePhone", cursor.getString(2));
                    contactToModify.putString("cEmail", cursor.getString(3));
                    contactToModify.putString("mod_type", "modifyPerson");
                    Intent intent = new Intent(this, ContactDetails.class);
                    intent.setClass(this, ContactDetails.class);
                    intent.putExtras(contactToModify);
                    startActivityForResult(intent, CONTACT_MODIFIED);
                } else {
                    Toast.makeText(this, "Select Contact to modify", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.additem:
                Intent i = new Intent(this, ContactDetails.class);
                Bundle bun = new Bundle();
                bun.putString("mod_type", "addPerson");
                i.setClass(this, ContactDetails.class);
                i.putExtras(bun);
                startActivityForResult(i, CONTACT_ADDED);
                break;
            case R.id.removeitem:
                if (null != itemId) {
                    removeContact(itemId);
                    showDatabaseContent();
                } else {
                    Toast.makeText(this, "Select Contact to delete", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(resultCode) {
            case CONTACT_ADDED:
                if (resultCode == RESULT_FIRST_USER) {
                    Bundle bundle = new Bundle();
                    bundle = intent.getBundleExtra("contactData");
                    addContact(bundle);
                    showDatabaseContent();
                } else {
                    Toast.makeText(this, "CANCEL CONTACT BUTTON PRESSED", Toast.LENGTH_LONG).show();
                }
                break;
            case CONTACT_MODIFIED:
                if (resultCode == 2) {
                    Bundle bundle = new Bundle();
                    bundle = intent.getBundleExtra("contactData");
                    modifyContact(bundle);
                    showDatabaseContent();
                } else {
                    Toast.makeText(this, "MODIFY CONTACT FAILED", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private void removeContact(String itemId) {
        db = contacts.getWritableDatabase();
        db.delete(DbConstants.TABLE_NAME, "_ID=" + itemId, null);
    }

    private void addContact(Bundle bundle) {
        db = contacts.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(DbConstants.NAME, bundle.getString("contactFirstName"));
        vals.put(DbConstants.PHONE, bundle.getString("contactMobilePhone"));
        vals.put(DbConstants.EMAIL, bundle.getString("contactEmail"));
        db.insertOrThrow(DbConstants.TABLE_NAME, null, vals);
    }

    private void modifyContact(Bundle bundle) {
        db = contacts.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(DbConstants.NAME, bundle.getString("contactFirstName"));
        vals.put(DbConstants.PHONE, bundle.getString("contactMobilePhone"));
        vals.put(DbConstants.EMAIL, bundle.getString("contactEmail"));
        db.update(DbConstants.TABLE_NAME, vals, _ID + "=" + itemId, null);
    }

    private Cursor getContacts() {
        db = contacts.getReadableDatabase();
        cursor = db.query(DbConstants.TABLE_NAME, FROM, null, null, null, null, null);
        startManagingCursor(cursor);
        return cursor;
    }

    public void showDatabaseContent() {
        contacts = new DbCreate(this);
        try {
            cursor = getContacts();
            showContacts(cursor);
        } finally {
            contacts.close();
            db.close();
        }
    }

    private void showContacts(Cursor cursor) {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, FROM, TO);
        setListAdapter(adapter);
    }
}
