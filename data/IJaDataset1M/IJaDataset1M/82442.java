package com.habitsoft.glassfish.dashboard.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author dobes
 * @generated generated asynchronous callback interface to be used on the client side
 *
 */
public interface LogServiceAsync {

    /**
     * @param  callback the callback that will be called to receive the return value
     * @generated generated method with asynchronous callback parameter to be used on the client side
     */
    void countLogMessages(String logFile, String filter, AsyncCallback<Integer> callback);

    /**
     * @param  callback the callback that will be called to receive the return value
     * @generated generated method with asynchronous callback parameter to be used on the client side
     */
    void getLogMessages(String logFile, String filter, int offset, int limit, AsyncCallback<String[]> callback);

    /**
     * @param  callback the callback that will be called to receive the return value
     * @generated generated method with asynchronous callback parameter to be used on the client side
     */
    void listLogFiles(AsyncCallback<String[]> callback);
}
