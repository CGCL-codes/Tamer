package edu.upmc.opi.caBIG.caTIES.status.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("search")
public interface SearchService extends RemoteService {

    public ServiceStatus checkStatus();
}
