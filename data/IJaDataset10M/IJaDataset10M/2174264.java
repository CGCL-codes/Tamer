package org.thechiselgroup.chooselexample.client.services;

import org.thechiselgroup.choosel.core.client.util.ServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("proxy")
public interface ProxyService extends RemoteService {

    String fetchURL(String url) throws ServiceException;
}
