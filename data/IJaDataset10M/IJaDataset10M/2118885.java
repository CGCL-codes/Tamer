package tms.client.services;

import tms.client.accesscontrol.User;
import tms.client.exceptions.DataOperationException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("userRetriever")
public interface UserRetrievalService extends RemoteService {

    public User retrieveUserByUsername(String username, String password) throws DataOperationException;
}
