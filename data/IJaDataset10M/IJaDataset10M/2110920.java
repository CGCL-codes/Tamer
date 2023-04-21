package com.abbsil.ebr.dao;

import java.util.HashMap;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class EbookreaderProvider extends ContentProvider {

    private static final String TAG = "EbookreaderProvider";

    private static final String DATABASE_NAME = "ebook_reader.db";

    private static final int DATABASE_VERSION = 1;

    private static final String LIVRES_TABLE_NAME = "notes";

    private static final String GENRES_TABLE_NAME = "genres";

    private static final String AUTEURS_TABLE_NAME = "auteurs";

    private static final String CHAPITRES_TABLE_NAME = "chapitres";

    private static HashMap<String, String> sNotesProjectionMap;

    private static HashMap<String, String> sLiveFolderProjectionMap;

    private static final int NOTES = 1;

    private static final int NOTE_ID = 2;

    private static final int LIVE_FOLDER_NOTES = 3;

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) {
        return null;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        return 0;
    }
}
