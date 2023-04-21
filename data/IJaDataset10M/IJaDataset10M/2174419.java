package org.brainsatwork.mediasuite.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author jenstegelaers
 */
public interface DataStorageServiceAsync {

    public void testCall(String s, AsyncCallback<String> callback);

    public void createAdvertiser(String s, AsyncCallback<String> callback);

    public void createCampaign(String s, AsyncCallback<String> callback);

    public void getAdvertiser(AsyncCallback<String> callback);

    public void getCampaign(AsyncCallback<String> callback);

    public void deleteSomething(AsyncCallback<String> asyncCallback);

    public void createMediaSchedule(String s, AsyncCallback<String> asyncCallback);
}
