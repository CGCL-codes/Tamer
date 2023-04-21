package org.messageforge.quickfixj.admin.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SessionMBeanEventServiceAsync {

    void getSnapshot(AsyncCallback<SessionAdminMBeanChangedEvent[]> callback);

    void getHistory(AsyncCallback<SessionAdminMBeanChangedEvent[]> callback);
}
