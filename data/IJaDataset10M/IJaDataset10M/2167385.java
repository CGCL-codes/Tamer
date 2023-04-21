package com.busfm.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import com.busfm.model.DownloadEntity;
import com.busfm.model.SongEntity;
import com.busfm.provider.DBProvider;

/**
 * <p>
 * Title:DBHelper
 * </p>
 * <p>
 * Description: DBHelper
 * </p>
 * <p>
 * Copyright (c) 2011 www.bus.fm Inc. All rights reserved.
 * </p>
 * <p>
 * Company: bus.fm
 * </p>
 * 
 * 
 * @author jingguo0@gmail.com
 * 
 */
public class DBHelper {

    public static void addToDownloadDB(DownloadEntity downloadEntity) {
        ContentValues values = new ContentValues();
        values.put(DBProvider.COL_SONGID, downloadEntity.songId);
        values.put(DBProvider.COL_NAME, downloadEntity.title);
        values.put(DBProvider.COL_PATH, downloadEntity.path);
        values.put(DBProvider.COL_ALBUM, downloadEntity.album);
        values.put(DBProvider.COL_ARTIST, downloadEntity.artist);
        values.put(DBProvider.COL_TOTAL_SIZE, downloadEntity.totalSize);
        values.put(DBProvider.COL_DOWNLOADED, downloadEntity.isDownloaded);
        Uri uri = PrefUtil.getContext().getContentResolver().insert(DBProvider.CONTENT_URI_DOWNLOAD, values);
    }

    public static DownloadEntity getDownloadEntity(String songId) {
        DownloadEntity downloadEntity = new DownloadEntity();
        final ContentResolver contentResolver = PrefUtil.getContext().getContentResolver();
        String selection = DBProvider.COL_SONGID + "=?";
        String[] selectionArgs = new String[] { songId };
        Cursor cursor = contentResolver.query(DBProvider.CONTENT_URI_DOWNLOAD, null, selection, selectionArgs, null);
        if (cursor.getCount() > 0 & cursor.moveToFirst()) {
            downloadEntity.songId = cursor.getString(cursor.getColumnIndex(DBProvider.COL_SONGID));
            downloadEntity.title = cursor.getString(cursor.getColumnIndex(DBProvider.COL_NAME));
            downloadEntity.path = cursor.getString(cursor.getColumnIndex(DBProvider.COL_PATH));
            downloadEntity.album = cursor.getString(cursor.getColumnIndex(DBProvider.COL_ALBUM));
            downloadEntity.artist = cursor.getString(cursor.getColumnIndex(DBProvider.COL_ARTIST));
            downloadEntity.totalSize = cursor.getInt(cursor.getColumnIndex(DBProvider.COL_TOTAL_SIZE));
            downloadEntity.songId = cursor.getString(cursor.getColumnIndex(DBProvider.COL_SONGID));
            return downloadEntity;
        }
        return null;
    }
}
