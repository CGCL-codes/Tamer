package mobilesearch.thread;

import android.content.ContentResolver;
import mobilesearch.data.ImageData;
import mobilesearch.ui.BroadcastCommand;
import java.util.ArrayList;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.content.Intent;
import android.content.Context;

public class ImageSearchThread extends Thread {

    private static final String TAG = "ImageSearchThread";

    private ContentResolver resolver;

    private Context context;

    private String imageId;

    private String imageName;

    private String imageTime;

    public ImageSearchThread(Context context, ContentResolver resolver) {
        super();
        this.context = context;
        this.resolver = resolver;
    }

    @Override
    public void run() {
        super.run();
        ArrayList<ImageData> imageList = new ArrayList<ImageData>();
        Cursor cursor = null;
        try {
            cursor = this.resolver.query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, null, null, null, null);
            if (cursor.moveToFirst()) {
                Log.i(TAG, "INTERNAL_CONTENT_URI");
                this.Search(cursor, imageList);
            }
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
        try {
            cursor = this.resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (cursor.moveToFirst()) {
                Log.i(TAG, "EXTERNAL_CONTENT_URI");
                this.Search(cursor, imageList);
            }
        } catch (Exception exception) {
            Log.i(TAG, exception.toString());
        }
        if (!imageList.isEmpty()) {
            Intent imageIntent = new Intent(BroadcastCommand.SEARCH_IMAGE);
            imageIntent.putParcelableArrayListExtra(BroadcastCommand.SEARCH_IMAGE, imageList);
            this.context.sendBroadcast(imageIntent);
        }
    }

    private void Search(Cursor cursor, ArrayList<ImageData> imageList) {
        do {
            ImageData data = new ImageData();
            int idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            this.imageId = cursor.getString(idIndex);
            data.setImageId(this.imageId);
            Log.i(TAG, this.imageId);
            int imageNameIndex = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
            this.imageName = cursor.getString(imageNameIndex);
            data.setImageName(imageName);
            Log.i(TAG, this.imageName);
            int timeIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            this.imageTime = cursor.getString(timeIndex);
            data.setImageTime(imageTime);
            Log.i(TAG, this.imageTime);
            imageList.add(data);
        } while (cursor.moveToNext());
    }
}
