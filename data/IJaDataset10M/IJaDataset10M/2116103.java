package net.sipvipbase.client;

import net.sipvipbase.client.JSONRequestHandler;

public class JSONRequest {

    public static void get(String url, JSONRequestHandler handler) {
        String callbackName = "JSONCallback" + handler.hashCode();
        get(url + callbackName, callbackName, handler);
    }

    public static void get(String url, String callbackName, JSONRequestHandler handler) {
        createCallbackFunction(handler, callbackName);
        addScript(url);
    }

    public static native void addScript(String url);

    private static native void createCallbackFunction(JSONRequestHandler obj, String callbackName);
}
