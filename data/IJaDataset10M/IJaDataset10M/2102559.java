package cz.krtinec.telka.images;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;

public class ImageCache {

    private static ImageCache instance = new ImageCache();

    private Map<String, Drawable> cache;

    public static final Drawable TELKA = Drawable.createFromPath("res/telka.png");

    protected ImageCache() {
        this.cache = new WeakHashMap<String, Drawable>();
    }

    public static ImageCache getInstance() {
        return instance;
    }

    public Drawable getImage(String url, Context ctx) {
        Drawable d = cache.get(url);
        if (d == null) {
            d = this.fetchImage(url, ctx);
            cache.put(url, d);
        }
        return d;
    }

    protected Drawable fetchImage(String iconUrl) {
        return this.fetchImage(iconUrl, null);
    }

    private Drawable fetchImage(String iconUrl, Context ctx) {
        URL url;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            if (PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("use.urlimg.com", true)) {
                iconUrl = iconUrl.substring(iconUrl.indexOf("//") + 2);
                iconUrl = "http://urlimg.com/width/100/" + iconUrl;
            }
            Log.d(ImageCache.class.getName(), "Loading image from: " + iconUrl);
            HttpGet httpGet = new HttpGet(iconUrl);
            HttpResponse response = httpClient.execute(httpGet);
            InputStream content = response.getEntity().getContent();
            Drawable d = Drawable.createFromStream(content, "src");
            content.close();
            httpGet.abort();
            return d;
        } catch (IOException e) {
            Log.e(ImageCache.class.getName(), "IOException while fetching: " + iconUrl);
            return TELKA;
        } finally {
        }
    }
}
