package com.whale.util.image;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

/**
 * 图片管理工具类,主要负责图片的压缩、裁剪等，不涉及异步下载和缓存的实现(将另外实现)
 * 
 * @author lzk
 * 
 */
public class ImageCanvas {

    private static final String TAG = "ImageManager";

    private static final String IMAGE = "upload.jpg";

    public static final int IMAGE_MAX_WIDTH = 480;

    public static final int IMAGE_MAX_HEIGHT = 800;

    private Context mContext;

    public ImageCanvas(Context context) {
        mContext = context;
    }

    /**
	 * 制作图片的缩率图
	 * 
	 * @param uri
	 *            图片路径
	 * @param size
	 *            多大比例时开始压缩
	 * @return
	 */
    public Bitmap createThumbnailBitmap(Uri uri, int size) {
        InputStream input = null;
        try {
            input = mContext.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);
            input.close();
            int scale = 1;
            while ((options.outWidth / scale > size) || (options.outHeight / scale > size)) {
                scale *= 2;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            input = mContext.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(input, null, options);
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.w(TAG, e);
                }
            }
        }
    }

    /**
	 * 按照宽度、高度做一定比例压缩，从而减少大图片在网络传输中浪费流量以及用户等待时间
	 * 
	 * @param targetFile
	 * @param quality
	 */
    public File compressImage(File targetFile, int quality) throws IOException {
        String filepath = targetFile.getAbsolutePath();
        int scale = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        if (options.outWidth > IMAGE_MAX_WIDTH || options.outHeight > IMAGE_MAX_HEIGHT) {
            scale = (int) Math.pow(2.0, (int) Math.round(Math.log(IMAGE_MAX_WIDTH / (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
        writeFile(filepath, bitmap, quality);
        String filePath = IMAGE;
        File compressedImage = mContext.getFileStreamPath(filePath);
        return compressedImage;
    }

    /**
	 * 将Bitmap写入本地缓存文件.
	 * 
	 * @param file
	 *            URL/PATH
	 * @param bitmap
	 * @param quality
	 */
    private void writeFile(String file, Bitmap bitmap, int quality) {
        if (bitmap == null) {
            Log.w(TAG, "Can't write file. Bitmap is null.");
            return;
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(mContext.openFileOutput(IMAGE, Context.MODE_PRIVATE));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } finally {
            try {
                if (bos != null) {
                    bitmap.recycle();
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Could not close file.");
            }
        }
    }
}
