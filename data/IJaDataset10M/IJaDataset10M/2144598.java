package com.pustral.comvey.client.servlet;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pustral.comvey.shared.SessionData;

@RemoteServiceRelativePath("user-account")
public interface IUserAccountServ extends RemoteService {

    String[] getDataSource(String[] fieldNames);

    SessionData login(String username, String password) throws Exception;

    void logout(SessionData sessionData);
}
