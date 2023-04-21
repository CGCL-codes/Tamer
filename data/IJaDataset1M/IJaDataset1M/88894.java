package uk.gov.snh.android.biorecorder.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageUtil {

    public static Bitmap getThumbnail(ContentResolver cr, Uri imageUri, int kind) {
        if (imageUri == null || imageUri.toString().equals("")) return null;
        int imageId = getImageId(cr, imageUri);
        return MediaStore.Images.Thumbnails.getThumbnail(cr, imageId, kind, (BitmapFactory.Options) null);
    }

    private static int getImageId(ContentResolver cr, Uri uri) {
        int imageId = -1;
        String[] projection = { MediaStore.Images.Media._ID };
        Cursor cursor = cr.query(uri, projection, null, null, null);
        if (cursor.moveToFirst()) imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
        cursor.close();
        return imageId;
    }
}
