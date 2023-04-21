package com.google.gwt.gadgets.client.rpc;

/**
 * Provides access to the gadgets.rpc feature.
 * 
 * This is just a skeleton implementation that will add the RPC feature to the
 * gadget spec and allow a developer to write JSNI methods to access RPC from
 * JavaScript. For example:
 * 
 * <pre>
 *   public static native void callMyRpc(String arg1, int arg2, JavaScriptObject arg3)
 *    ...
 *    $wnd.gadgets.rpc.call("targetId", "serviceName",  arg1, arg2, arg3, function(callbackArg) {
 *      ...
 *    });
 * </pre>
 * 
 * @see "http://code.google.com/p/gwt-google-apis/issues/detail?id=390"
 */
public interface RpcFeature {

    /**
   * Unregisters the default service handler.
   */
    void unregisterDefault();
}
