package com.realsnake.ui4;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        GridView gv = (GridView) this.findViewById(R.id.gridview1);
        Cursor c = this.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
        startManagingCursor(c);
        String[] cols = new String[] { Phone.DISPLAY_NAME };
        int[] names = new int[] { android.R.id.text1 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, cols, names);
        gv.setAdapter(adapter);
    }
}
