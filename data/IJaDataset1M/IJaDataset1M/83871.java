package com.custom.provider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.custom.provider.NotePad.Notes;
import com.custom.view.R;

public class ProviderTest extends Activity {

    ListView lv = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider);
        lv = (ListView) this.findViewById(R.id.ListView01);
        ContentValues cv = new ContentValues();
        cv.put(Notes.TITLE, "title1");
        cv.put(Notes.NOTE, "note1");
        this.getContentResolver().insert(Notes.CONTENT_URI, cv);
        cv.clear();
        cv.put(Notes.TITLE, "title2");
        cv.put(Notes.NOTE, "note2");
        this.getContentResolver().insert(Notes.CONTENT_URI, cv);
        this.displayNote();
    }

    private void displayNote() {
        String[] columns = new String[] { Notes._ID, Notes.TITLE, Notes.NOTE };
        Cursor c = this.managedQuery(Notes.CONTENT_URI, columns, null, null, null);
        this.startManagingCursor(c);
        if (c != null) {
            int cs = 0;
            if (c.isBeforeFirst()) {
                cs++;
                this.setTitle("isBeforeFirst" + cs);
            }
            if (c.moveToFirst()) {
                cs++;
                this.setTitle("moveToFirst" + cs);
            }
            if (c.isFirst()) {
                cs++;
                this.setTitle("isFirst" + cs);
            }
            ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, new String[] { Notes._ID, Notes.TITLE }, new int[] { android.R.id.text1, android.R.id.text2 });
            lv.setAdapter(adapter);
        }
    }
}
