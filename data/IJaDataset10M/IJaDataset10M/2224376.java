package org.messageforge.quickfixj.admin.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("events.rpc")
public interface SessionMBeanEventService extends RemoteService {

    public abstract SessionAdminMBeanChangedEvent[] getSnapshot();

    public SessionAdminMBeanChangedEvent[] getHistory();
}
