package com.android.rpc_performance;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class Provider extends ContentProvider {

    public boolean onCreate() {
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        MatrixCursor c = new MatrixCursor(new String[] { "value" }, 1);
        c.addRow(new String[] { "my_value" });
        return c;
    }

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public String getType(Uri uri) {
        return "text/plain";
    }
}
