package com.turnengine.client.local.player.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceAsync;
import com.turnengine.client.local.player.bean.IPlayer;

/**
 * The Get Player By Name Service Async.
 */
@GwtCompatible
public interface GetPlayerByNameServiceAsync extends IGeneratedRemoteServiceAsync {

    void getPlayerByName(long loginId, int instanceId, String name, AsyncCallback<IPlayer> calback);
}
