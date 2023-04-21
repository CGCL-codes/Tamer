package cz.krtinec.telka.images;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class ImageCache16 extends ImageCache {

    private static ImageCache myinstance = new ImageCache16();

    private ImageCache16() {
        super();
    }

    public static ImageCache getInstance() {
        return myinstance;
    }

    protected Drawable fetchImage(String iconUrl, Context ctx) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        Log.i("Main", "fetching " + iconUrl);
        URL url;
        try {
            url = new URL(iconUrl);
            InputStream is = (InputStream) url.getContent();
            Bitmap bm = BitmapFactory.decodeStream(is);
            if (bm != null) {
                BitmapDrawable bd = new BitmapDrawable(bm);
                bd.setTargetDensity(metrics);
                return bd;
            }
            return null;
        } catch (IOException e) {
            Log.e("Main", "IOException while fetching...");
        }
        return null;
    }
}
