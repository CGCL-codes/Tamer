package com.turnengine.client.local.action.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceAsync;
import com.turnengine.client.local.action.bean.IAction;

/**
 * The Add Action Service Async.
 */
@GwtCompatible
public interface AddActionServiceAsync extends IGeneratedRemoteServiceAsync {

    void addAction(long loginId, int instanceId, int actionId, int turns, int limit, AsyncCallback<IAction> calback);
}
