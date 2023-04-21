package com.google.gwt.sample.mail.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.mail.shared.SessionIdResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("LogininService")
public interface LogininService extends RemoteService {

    SessionIdResult login(String name) throws IllegalArgumentException;

    SessionIdResult checkIfSessionIdIsStillLegal(String id);
}
