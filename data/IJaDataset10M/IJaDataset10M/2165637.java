package org.labrad.browser.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegistryServiceAsync {

    public void getListing(List<String> path, AsyncCallback<RegistryListing> callback);

    public void set(List<String> path, String key, String value, AsyncCallback<RegistryListing> callback);

    public void del(List<String> path, String dir, AsyncCallback<RegistryListing> callback);

    public void mkdir(List<String> path, String dir, AsyncCallback<RegistryListing> callback);

    public void rmdir(List<String> path, String dir, AsyncCallback<RegistryListing> callback);
}
