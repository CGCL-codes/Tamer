package cc.carnero.ctwee;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ctImageLoader {

    private final HashMap<String, SoftReference<Drawable>> avatarCache = new HashMap<String, SoftReference<Drawable>>();

    private static final Boolean sdMounted = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED);

    private final File dir = new File(ctGlobal.getStorage());

    public void checkCacheDir() {
        if (sdMounted == true) {
            if (dir.exists() == false) {
                dir.mkdir();
                Log.i(ctGlobal.tag, "Created cache directory on SD card");
            }
        }
    }

    public static Drawable loadDrawableFromCache(final String user) {
        final String filePath = ctGlobal.getStorage() + ctCommon.md5(user.toLowerCase());
        final File fileName = new File(filePath);
        try {
            if (sdMounted == true) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, ctGlobal.avatarTime);
                Date dateLimit = calendar.getTime();
                Date dateFile = new Date((long) fileName.lastModified());
                if (fileName.exists() == true && fileName.canRead() == true && dateLimit.compareTo(dateFile) < 0) {
                    Drawable drawable = Drawable.createFromPath(filePath);
                    return drawable;
                }
            }
        } catch (Exception e) {
            Log.e(ctGlobal.tag, "ctImageLoader.loadDrawableFromCache: " + e.toString());
        }
        return null;
    }

    public Drawable loadDrawable(final String name, final String imageUrl, final ImageCallback imageCallback) {
        final String filePath = ctGlobal.getStorage() + ctCommon.md5(name.toLowerCase());
        final File fileName = new File(filePath);
        try {
            if (avatarCache.containsKey(name) == true) {
                SoftReference<Drawable> softReference = avatarCache.get(name);
                Drawable drawable = softReference.get();
                if (drawable != null) {
                    return drawable;
                }
            }
            if (sdMounted == true) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, ctGlobal.avatarTime);
                Date dateLimit = calendar.getTime();
                Date dateFile = new Date((long) fileName.lastModified());
                if (fileName.exists() == true && fileName.canRead() == true && dateLimit.compareTo(dateFile) < 0) {
                    Drawable drawable = Drawable.createFromPath(filePath);
                    avatarCache.put(name, new SoftReference<Drawable>(drawable));
                    return drawable;
                }
            }
            final Handler handler = new Handler() {

                @Override
                public void handleMessage(Message message) {
                    imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
                }
            };
            new Thread() {

                @Override
                public void run() {
                    Drawable drawable = null;
                    if (sdMounted == true) {
                        saveToCache(imageUrl, filePath);
                        drawable = Drawable.createFromPath(filePath);
                    } else {
                        drawable = loadImageFromUrl(imageUrl);
                    }
                    Message message = handler.obtainMessage(0, drawable);
                    handler.sendMessage(message);
                }
            }.start();
        } catch (Exception e) {
            Log.e(ctGlobal.tag, "ctImageLoader.loadDrawable: " + e.toString());
        }
        return null;
    }

    public static void saveToCache(String imageUrl, String filePath) {
        try {
            URL request = new URL(imageUrl);
            InputStream is = (InputStream) request.getContent();
            FileOutputStream fos = new FileOutputStream(filePath);
            try {
                byte[] buffer = new byte[4096];
                int l;
                while ((l = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, l);
                }
                Log.i(ctGlobal.tag, "Cached avatar " + imageUrl + " to SD card");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                is.close();
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            Log.e(ctGlobal.tag, "ctImageLoader.saveToCache: " + e.toString());
        }
    }

    public Drawable loadImageFromUrl(String url) {
        InputStream inputStream = null;
        Drawable image = null;
        try {
            inputStream = new URL(url).openStream();
            image = Drawable.createFromStream(inputStream, "src");
        } catch (Exception e) {
            Log.e(ctGlobal.tag, "ctImageLoader.loadImageFromUrl: " + e.toString());
        }
        return image;
    }

    public interface ImageCallback {

        public void imageLoaded(Drawable imageDrawable, String imageUrl);
    }
}
