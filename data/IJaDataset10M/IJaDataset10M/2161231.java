package net.deytan.wofee.gwt.client;

import net.deytan.wofee.gwt.bean.OpmlFileBean;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTOpmlServiceAsync {

    void read(String file, AsyncCallback<OpmlFileBean> callback);

    void addFeeds(OpmlFileBean opmlFileBean, AsyncCallback<String> callback);
}
