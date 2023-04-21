package net.infordata.android.ifw2wv;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.WebView;

public class Util {

    private static final String VERSION = "002";

    public static final String LOGTAG = "IFW2WV";

    public static String getVersion() {
        return VERSION;
    }

    public static void dowloadResource(WebView view, String url, File file) throws IOException {
        String cookie = CookieManager.getInstance().getCookie(url);
        MyHttpClient client = new MyHttpClient();
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", view.getSettings().getUserAgentString());
        request.setHeader("Cookie", cookie);
        HttpResponse response = client.execute(request);
        FileOutputStream fos = new FileOutputStream(file);
        HttpEntity ent = response.getEntity();
        ent.writeTo(fos);
        ent.consumeContent();
        request.abort();
        fos.close();
    }

    public static byte[] dowloadResource(WebView view, String url) throws IOException {
        String cookie = CookieManager.getInstance().getCookie(url);
        MyHttpClient client = new MyHttpClient();
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", view.getSettings().getUserAgentString());
        request.setHeader("Cookie", cookie);
        HttpResponse response = client.execute(request);
        TmpByteArrayOutputStream fos = new TmpByteArrayOutputStream(2048);
        HttpEntity ent = response.getEntity();
        ent.writeTo(fos);
        ent.consumeContent();
        request.abort();
        fos.close();
        return fos.toByteArray();
    }

    public static Properties retrieveProperties(WebView view, Uri uri) throws IOException {
        final String scheme = uri.getScheme();
        InputStream is = null;
        if ("http".equals(scheme) || "https".equals(scheme)) {
            byte[] buf = dowloadResource(view, uri.toString());
            is = new ByteArrayInputStream(buf);
        } else if ("file".equals(scheme)) {
            is = new FileInputStream(uri.getPath());
        } else if ("content".equals(scheme)) {
            ContentResolver resolver = view.getContext().getContentResolver();
            is = resolver.openInputStream(uri);
        }
        if (is == null) {
            throw new IllegalArgumentException("Cannot retrieve properties from:" + uri.toString());
        }
        Properties res = new Properties();
        res.load(is);
        is.close();
        return res;
    }
}
