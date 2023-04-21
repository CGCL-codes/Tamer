package com.example.android.apis.view;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract.Contacts;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.SimpleCursorAdapter;
import android.widget.SpinnerAdapter;
import com.example.android.apis.R;

public class Gallery2 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_2);
        Cursor c = getContentResolver().query(Contacts.CONTENT_URI, CONTACT_PROJECTION, null, null, null);
        startManagingCursor(c);
        SpinnerAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_gallery_item, c, new String[] { Contacts.DISPLAY_NAME }, new int[] { android.R.id.text1 });
        Gallery g = (Gallery) findViewById(R.id.gallery);
        g.setAdapter(adapter);
    }

    private static final String[] CONTACT_PROJECTION = new String[] { Contacts._ID, Contacts.DISPLAY_NAME };
}
