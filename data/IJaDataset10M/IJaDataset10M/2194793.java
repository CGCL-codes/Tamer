package org.feeddreamwork;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public final class Utils {

    private static Pattern absoluteURLPattern = Pattern.compile("^[a-z0-9]*:.*$");

    public static boolean isNullOrEmpty(String source) {
        return source == null || source.isEmpty();
    }

    public static void throwIfNull(Object source) {
        if (source == null) throw new IllegalArgumentException();
    }

    public static void throwIfNullOrEmpty(String source) {
        if (Utils.isNullOrEmpty(source)) throw new IllegalArgumentException();
    }

    public static void throwIfSmallerThan(int source, int limit) {
        if (source < limit) throw new IllegalArgumentException();
    }

    public static void throwIfDevelopment(Exception e) {
        if (ApplicationProperty.IS_DEVELOPMENT) throw new RuntimeException(e);
    }

    public static URL resolveURL(String source, String baseURL) throws MalformedURLException {
        if (Utils.isNullOrEmpty(baseURL)) return new URL(modifyURL(source));
        baseURL = modifyURL(baseURL);
        return new URL(new URL(baseURL), source);
    }

    public static boolean isAbsoluteURL(String url) {
        return absoluteURLPattern.matcher(url).find();
    }

    public static void sendHttpPost(String target, Map<String, String> context, int desiredResponseCode) throws IOException {
        int retry = ApplicationProperty.HTTP_RETRY_TIMES;
        while (true) try {
            URL url = new URL(target);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            boolean isFirst = true;
            for (Map.Entry<String, String> entry : context.entrySet()) {
                if (isFirst) isFirst = false; else writer.write('&');
                writer.write(URLEncoder.encode(entry.getKey(), "UTF-8"));
                writer.write('=');
                writer.write(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            writer.close();
            if (connection.getResponseCode() == desiredResponseCode) return; else throw new IOException();
        } catch (IOException e) {
            retry--;
            if (retry == 0) throw e;
        }
    }

    private static String modifyURL(String source) {
        if (!source.contains("://")) return "http://" + source;
        return source;
    }

    private Utils() {
    }
}
