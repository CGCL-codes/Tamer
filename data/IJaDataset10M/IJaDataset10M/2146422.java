package com.turnpage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageUtils {

    public static final int DEFAULT_COVER_HEIGHT = 105;

    public static final int DEFAULT_COVER_WIDTH = 84;

    public static Bitmap defaultBitmap;

    public static Bitmap coverFrameworkBitmap;

    /**
	 * ��ȡָ���ļ�ת����ͼƬ
	 * @param imagePath
	 * @return
	 */
    public static Bitmap getImageByPath(String imagePath) {
        Bitmap resultBitmap = null;
        if (imagePath != null && !"".equals(imagePath.trim())) {
            File coverFile = new File(imagePath);
            if (coverFile.exists()) {
                if (coverFile.isFile()) {
                    resultBitmap = BitmapFactory.decodeFile(coverFile.getAbsolutePath());
                    Log.v("BookReader", "isFile");
                }
            } else {
                Log.v("BookReader", "UnExists");
            }
        }
        return resultBitmap;
    }

    /**
	 * �ı�ͼƬ��С
	 * @param newWidth
	 * @param newHight
	 * @param oldBitmap
	 * @return
	 */
    public static Bitmap changeBitmapSize(float newWidth, float newHight, Bitmap oldBitmap) {
        if (oldBitmap == null) {
            return null;
        }
        float bmpWidth = oldBitmap.getWidth();
        float bmpHeight = oldBitmap.getHeight();
        if (bmpWidth == newWidth && newHight == bmpHeight) {
            return oldBitmap;
        }
        if (bmpWidth == newWidth && bmpHeight == newHight) {
            return oldBitmap;
        }
        float scaleWidth = newWidth / bmpWidth;
        float scaleHeight = newHight / bmpHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bm = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);
        oldBitmap.recycle();
        return bm;
    }

    public static String printlnBitmap(Bitmap bm, CompressFormat format) {
        FileOutputStream fos = null;
        String filePath = null;
        long bitmapSize = bm.getRowBytes() * bm.getHeight();
        if (!SDCardUtils.isSDCardEnable()) {
            return null;
        } else if (SDCardUtils.getFreeSize() <= bitmapSize) {
            return null;
        }
        String rootPath = FileUtils.NETWORK_IMAGE_CACHE_DIRECTORY + "cache";
        try {
            File file = new File(rootPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            while (true) {
                int rd = Math.abs((new Random()).nextInt());
                if (format == CompressFormat.PNG) filePath = rootPath + File.separator + rd + ".png"; else {
                    filePath = rootPath + File.separator + rd + ".jpg";
                }
                file = new File(filePath);
                if (file.exists()) {
                    continue;
                }
                break;
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bm.compress(format, 75, fos);
            fos.flush();
            fos.close();
            return filePath;
        } catch (Exception e) {
            Log.e("BookReader", e.toString());
        }
        return null;
    }

    public static Drawable drawableToBitmap(Bitmap bm) {
        return new BitmapDrawable(bm);
    }
}
