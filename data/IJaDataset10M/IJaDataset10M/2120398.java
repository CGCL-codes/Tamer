package com.escape.proxy;

/**
 * Proxy for Java Beans Introspector.
 * @author escape-llc
 *
 */
public class Introspector {

    public static String decapitalize(String target) {
        final StringBuffer sb = new StringBuffer();
        if (target.length() > 0) sb.append(Character.toLowerCase(target.charAt(0)));
        if (target.length() > 1) sb.append(target.substring(1));
        return sb.toString();
    }
}
