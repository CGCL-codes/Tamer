package com.turnengine.client.global.user.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceAsync;

/**
 * The Logout User Service Async.
 */
@GwtCompatible
public interface LogoutUserServiceAsync extends IGeneratedRemoteServiceAsync {

    void logoutUser(long loginId, AsyncCallback<Boolean> calback);
}
