package com.manning.aip.mymoviesdatabase;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.manning.aip.mymoviesdatabase.util.ImageCache;

public class DownloadListViewTask extends DownloadTask {

    private final long id;

    public DownloadListViewTask(ImageCache cache, long id, ImageView imageView) {
        super(cache, imageView);
        this.id = id;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        long forPosition = (Long) imageView.getTag();
        if (forPosition == this.id) {
            if (bitmap != null) {
                this.imageView.setImageBitmap(bitmap);
            }
        }
    }
}
