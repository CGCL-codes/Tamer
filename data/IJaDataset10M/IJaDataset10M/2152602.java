package org.imogene.android.sync.handler;

import java.lang.reflect.Method;
import org.imogene.android.common.Binary;
import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.content.ContentUrisUtils;
import org.xmlpull.v1.XmlPullParser;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class BinaryHandler<T> implements FieldHandler<T> {

    private final Method mMethod;

    public BinaryHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
        mMethod = c.getMethod(methodName, Uri.class);
    }

    public void parse(Context context, XmlPullParser parser, T entity) {
        try {
            String id = parser.nextText();
            ContentResolver res = context.getContentResolver();
            Cursor c = res.query(Binary.Columns.CONTENT_URI, new String[] { Binary.Columns._ID }, Binary.Columns._ID + "='" + id + "'", null, null);
            if (c.getCount() != 1) {
                c.close();
                ContentValues values = new ContentValues();
                values.put(Binary.Columns._ID, id);
                values.put(Binary.Columns.MODIFIEDFROM, Binary.Columns.SYNC_SYSTEM);
                mMethod.invoke(entity, res.insert(Binary.Columns.CONTENT_URI, values));
            } else {
                c.moveToFirst();
                String sId = c.getString(0);
                c.close();
                mMethod.invoke(entity, ContentUrisUtils.withAppendedId(Binary.Columns.CONTENT_URI, sId));
            }
        } catch (Exception e) {
            Log.e(BinaryHandler.class.getName(), "error parsing binary", e);
        }
    }
}
