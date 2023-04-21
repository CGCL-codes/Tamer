package com.android.mms.ui;

import com.android.mms.model.ImageModel;
import com.android.mms.LogTag;
import com.google.android.mms.pdu.PduPart;
import com.google.android.mms.util.SqliteWrapper;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.Telephony.Mms.Part;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class UriImage {

    private static final String TAG = "Mms/image";

    private static final boolean DEBUG = true;

    private static final boolean LOCAL_LOGV = DEBUG ? Config.LOGD : Config.LOGV;

    private final Context mContext;

    private final Uri mUri;

    private String mContentType;

    private String mPath;

    private String mSrc;

    private int mWidth;

    private int mHeight;

    public UriImage(Context context, Uri uri) {
        if ((null == context) || (null == uri)) {
            throw new IllegalArgumentException();
        }
        String scheme = uri.getScheme();
        if (scheme.equals("content")) {
            initFromContentUri(context, uri);
        } else if (uri.getScheme().equals("file")) {
            initFromFile(context, uri);
        }
        mSrc = mPath.substring(mPath.lastIndexOf('/') + 1);
        mSrc = mSrc.replace(' ', '_');
        mContext = context;
        mUri = uri;
        decodeBoundsInfo();
    }

    private void initFromFile(Context context, Uri uri) {
        mPath = uri.getPath();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extension = MimeTypeMap.getFileExtensionFromUrl(mPath);
        if (TextUtils.isEmpty(extension)) {
            int dotPos = mPath.lastIndexOf('.');
            if (0 <= dotPos) {
                extension = mPath.substring(dotPos + 1);
            }
        }
        mContentType = mimeTypeMap.getMimeTypeFromExtension(extension);
    }

    private void initFromContentUri(Context context, Uri uri) {
        Cursor c = SqliteWrapper.query(context, context.getContentResolver(), uri, null, null, null, null);
        if (c == null) {
            throw new IllegalArgumentException("Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() != 1) || !c.moveToFirst()) {
                throw new IllegalArgumentException("Query on " + uri + " returns 0 or multiple rows.");
            }
            String filePath;
            if (ImageModel.isMmsUri(uri)) {
                filePath = c.getString(c.getColumnIndexOrThrow(Part.FILENAME));
                if (TextUtils.isEmpty(filePath)) {
                    filePath = c.getString(c.getColumnIndexOrThrow(Part._DATA));
                }
                mContentType = c.getString(c.getColumnIndexOrThrow(Part.CONTENT_TYPE));
            } else {
                filePath = c.getString(c.getColumnIndexOrThrow(Images.Media.DATA));
                mContentType = c.getString(c.getColumnIndexOrThrow(Images.Media.MIME_TYPE));
            }
            mPath = filePath;
        } finally {
            c.close();
        }
    }

    private void decodeBoundsInfo() {
        InputStream input = null;
        try {
            input = mContext.getContentResolver().openInputStream(mUri);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, opt);
            mWidth = opt.outWidth;
            mHeight = opt.outHeight;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "IOException caught while opening stream", e);
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException caught while closing stream", e);
                }
            }
        }
    }

    public String getContentType() {
        return mContentType;
    }

    public String getSrc() {
        return mSrc;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public PduPart getResizedImageAsPart(int widthLimit, int heightLimit, int byteLimit) {
        PduPart part = new PduPart();
        byte[] data = getResizedImageData(widthLimit, heightLimit, byteLimit);
        if (data == null) {
            if (LOCAL_LOGV) {
                Log.v(TAG, "Resize image failed.");
            }
            return null;
        }
        part.setData(data);
        part.setContentType(getContentType().getBytes());
        String src = getSrc();
        byte[] srcBytes = src.getBytes();
        part.setContentLocation(srcBytes);
        part.setFilename(srcBytes);
        part.setContentId(src.substring(0, src.lastIndexOf(".")).getBytes());
        return part;
    }

    private static final int NUMBER_OF_RESIZE_ATTEMPTS = 4;

    private byte[] getResizedImageData(int widthLimit, int heightLimit, int byteLimit) {
        int outWidth = mWidth;
        int outHeight = mHeight;
        int scaleFactor = 1;
        while ((outWidth / scaleFactor > widthLimit) || (outHeight / scaleFactor > heightLimit)) {
            scaleFactor *= 2;
        }
        if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
            Log.v(TAG, "getResizedImageData: wlimit=" + widthLimit + ", hlimit=" + heightLimit + ", sizeLimit=" + byteLimit + ", mWidth=" + mWidth + ", mHeight=" + mHeight + ", initialScaleFactor=" + scaleFactor);
        }
        InputStream input = null;
        try {
            ByteArrayOutputStream os = null;
            int attempts = 1;
            do {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = scaleFactor;
                input = mContext.getContentResolver().openInputStream(mUri);
                int quality = MessageUtils.IMAGE_COMPRESSION_QUALITY;
                try {
                    Bitmap b = BitmapFactory.decodeStream(input, null, options);
                    if (b == null) {
                        return null;
                    }
                    if (options.outWidth > widthLimit || options.outHeight > heightLimit) {
                        int scaledWidth = outWidth / scaleFactor;
                        int scaledHeight = outHeight / scaleFactor;
                        if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
                            Log.v(TAG, "getResizedImageData: retry scaling using " + "Bitmap.createScaledBitmap: w=" + scaledWidth + ", h=" + scaledHeight);
                        }
                        b = Bitmap.createScaledBitmap(b, outWidth / scaleFactor, outHeight / scaleFactor, false);
                        if (b == null) {
                            return null;
                        }
                    }
                    os = new ByteArrayOutputStream();
                    b.compress(CompressFormat.JPEG, quality, os);
                    int jpgFileSize = os.size();
                    if (jpgFileSize > byteLimit) {
                        int reducedQuality = quality * byteLimit / jpgFileSize;
                        if (reducedQuality >= MessageUtils.MINIMUM_IMAGE_COMPRESSION_QUALITY) {
                            quality = reducedQuality;
                            if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
                                Log.v(TAG, "getResizedImageData: compress(2) w/ quality=" + quality);
                            }
                            os = new ByteArrayOutputStream();
                            b.compress(CompressFormat.JPEG, quality, os);
                        }
                    }
                } catch (java.lang.OutOfMemoryError e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                if (Log.isLoggable(LogTag.APP, Log.VERBOSE)) {
                    Log.v(TAG, "attempt=" + attempts + " size=" + (os == null ? 0 : os.size()) + " width=" + outWidth / scaleFactor + " height=" + outHeight / scaleFactor + " scaleFactor=" + scaleFactor + " quality=" + quality);
                }
                scaleFactor *= 2;
                attempts++;
            } while ((os == null || os.size() > byteLimit) && attempts < NUMBER_OF_RESIZE_ATTEMPTS);
            return os == null ? null : os.toByteArray();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }
}
