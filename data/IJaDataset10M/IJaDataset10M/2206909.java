package com.goodow.web.feature.client;

public class ApplicationCache {

    public interface Listener {

        void updateReady();
    }

    public static native void addEventListener(Listener listener);

    public static native void swapCache();
}
