package org.openintents.samples.lendme.data.persistence;

import org.openintents.samples.lendme.data.persistence.ItemsProviderHelper.ItemsTable;
import org.openintents.samples.lendme.data.persistence.ItemsProviderHelper.OpenHelper;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ItemsProvider extends ContentProvider {

    public static final String NAME = "ItemsProvider";

    private static UriMatcher sUriMatcher;

    private static final int MATCH_ITEMS = 1;

    private static final int MATCH_ITEM = 2;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(ItemsProviderHelper.AUTHORITY, ItemsProviderHelper.PATH_ITEMS, MATCH_ITEMS);
        sUriMatcher.addURI(ItemsProviderHelper.AUTHORITY, ItemsProviderHelper.PATH_ITEMS + "/#", MATCH_ITEM);
    }

    private OpenHelper openHelper;

    @Override
    public boolean onCreate() {
        openHelper = new OpenHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)) {
            case MATCH_ITEMS:
                return ItemsProviderHelper.CONTENT_TYPE;
            case MATCH_ITEM:
                return ItemsProviderHelper.ITEM_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = null;
        if (sUriMatcher.match(uri) == MATCH_ITEMS) {
            tableName = ItemsTable.TABLE_NAME;
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        Cursor retval = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        ;
        if (retval != null) retval.setNotificationUri(getContext().getContentResolver(), ItemsProviderHelper.CONTENT_URI);
        return retval;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = null;
        if (sUriMatcher.match(uri) == MATCH_ITEMS) {
            tableName = ItemsTable.TABLE_NAME;
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        long id = db.insert(tableName, null, values);
        if (id == -1) return null; else {
            getContext().getContentResolver().notifyChange(ItemsProviderHelper.CONTENT_URI, null);
            return Uri.withAppendedPath(ItemsProviderHelper.CONTENT_URI, String.valueOf(id));
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = null;
        if (sUriMatcher.match(uri) == MATCH_ITEM) {
            tableName = ItemsTable.TABLE_NAME;
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        long id = Long.parseLong(uri.getLastPathSegment());
        String where = ItemsTable._ID + " = " + id;
        getContext().getContentResolver().notifyChange(ItemsProviderHelper.CONTENT_URI, null);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db.delete(tableName, where, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
