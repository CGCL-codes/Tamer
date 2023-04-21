package org.apache.shindig.gadgets.rewrite;

import org.apache.shindig.gadgets.http.HttpRequest;
import org.apache.shindig.gadgets.http.HttpResponse;

/**
 * Various utility functions used by rewriters
 */
public class RewriterUtils {

    public static boolean isHtml(HttpRequest request, HttpResponse original) {
        String mimeType = getMimeType(request, original);
        return mimeType != null && (mimeType.contains("html"));
    }

    public static boolean isCss(HttpRequest request, HttpResponse original) {
        String mimeType = getMimeType(request, original);
        return mimeType != null && mimeType.contains("css");
    }

    public static boolean isJavascript(HttpRequest request, HttpResponse original) {
        String mimeType = getMimeType(request, original);
        return mimeType != null && mimeType.contains("javascript");
    }

    public static String getMimeType(HttpRequest request, HttpResponse original) {
        String mimeType = request.getRewriteMimeType();
        if (mimeType == null) {
            mimeType = original.getHeader("Content-Type");
        }
        return mimeType != null ? mimeType.toLowerCase() : null;
    }
}
