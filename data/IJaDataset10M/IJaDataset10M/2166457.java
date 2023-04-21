package com.nts.communicationmgmt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nts.communicationmgmt.shared.Users;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface LoginServiceAsync {

    void loginServer(String name, String pwd, AsyncCallback<Users> callback) throws IllegalArgumentException;
}
