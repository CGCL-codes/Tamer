package org.labrad.browser.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IpListServiceAsync {

    public void getIpList(AsyncCallback<List<IpAddress>> callback);

    public void addToWhitelist(String ip, AsyncCallback<List<IpAddress>> callback);

    public void addToBlacklist(String ip, AsyncCallback<List<IpAddress>> callback);
}
