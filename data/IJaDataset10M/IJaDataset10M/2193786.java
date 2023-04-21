package com.turnengine.client.global.user.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceAsync;
import com.turnengine.client.global.user.enums.UserPermissionType;

/**
 * The Set User Permission Service Async.
 */
@GwtCompatible
public interface SetUserPermissionServiceAsync extends IGeneratedRemoteServiceAsync {

    void setUserPermission(long loginId, int id, UserPermissionType type, AsyncCallback<Boolean> calback);
}
