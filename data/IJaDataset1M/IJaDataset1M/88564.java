package com.paullindorff.gwt.jaxrs.client.util;

/**
 * Base64 encoding utility using native JavaScript
 * Adapted from code at @see http://www.webtoolkit.info/javascript-base64.html
 * @author plindorff
 */
public class Base64 {

    private static String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static native String encode(String input);
}
