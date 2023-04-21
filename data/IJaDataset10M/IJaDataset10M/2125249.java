package com.hyper9.simdk.ws;

/**
 * A class for storing ThreadLocal context.
 * 
 * @author akutz
 * 
 */
public class Context {

    private static ThreadLocal<String> vimNamespace = new ThreadLocal<String>();

    public static void setVimNamespace(String toSet) {
        vimNamespace.set(toSet);
    }

    public static String getVimNamespace() {
        return vimNamespace.get();
    }
}
