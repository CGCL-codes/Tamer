package net.sf.andhsli.hotspotlogin;

import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.util.Log;

public class Helper {

    private static final String TAG = "HSLI.helper";

    public static String readRawResource(Context ctx, String name) {
        try {
            int id = ctx.getResources().getIdentifier(name, "raw", "net.sf.andhsli.hotspotlogin");
            if (id == 0) return null;
            InputStream is = ctx.getResources().openRawResource(id);
            int size = is.available();
            byte[] buf = new byte[size];
            is.read(buf);
            is.close();
            return new String(buf);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
