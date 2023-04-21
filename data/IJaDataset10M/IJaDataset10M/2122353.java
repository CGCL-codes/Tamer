package ssgpp.conn;

import ssgpp.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 *
 * @author Onur Derin <oderin at users.sourceforge.net>
 */
public class ConnectionUtils {

    public static String createPostString(String[][] data) throws UnsupportedEncodingException {
        String postStr = "";
        for (int i = 0; i < data.length; i++) {
            postStr += (URLEncoder.encode(data[i][0], "UTF-8") + "=" + URLEncoder.encode(data[i][1], "UTF-8"));
            if (i != data.length - 1) {
                postStr += "&";
            }
        }
        return postStr;
    }

    public static String responseString(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String decodedString;
        StringBuilder sb = new StringBuilder();
        while ((decodedString = in.readLine()) != null) {
            sb.append(decodedString + "\n");
        }
        in.close();
        return sb.toString();
    }

    public static void printResponse(InputStream is) throws IOException {
        System.out.println("####################");
        System.out.println("####################");
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String decodedString;
        while ((decodedString = in.readLine()) != null) {
            System.out.println(decodedString);
        }
        in.close();
        System.out.println("####################");
        System.out.println("####################");
    }

    public static void printCookies() {
        SchemeFreeCookieManager cm = (SchemeFreeCookieManager) CookieHandler.getDefault();
        System.out.println("####################");
        System.out.println("####################");
        System.out.println(cm.getCookieStore().getURIs());
        System.out.println("Cookieler:");
        CookieStore cookieJar = cm.getCookieStore();
        for (URI uri : cookieJar.getURIs()) {
            List<HttpCookie> cookies = cookieJar.get(uri);
            for (HttpCookie cookie : cookies) {
                System.out.println("Cookie: " + cookie + " from " + uri);
            }
        }
        System.out.println("####################");
        System.out.println("####################");
    }

    public static void printHeaders(URLConnection uc) {
        System.out.println("####################");
        System.out.println("####################");
        for (int j = 1; ; j++) {
            String header = uc.getHeaderField(j);
            if (header == null) break;
            System.out.println(uc.getHeaderFieldKey(j) + ": " + header);
        }
        System.out.println("####################");
        System.out.println("####################");
    }
}
