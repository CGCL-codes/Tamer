package com.turnengine.client.local.player.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteService;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;
import com.turnengine.client.local.player.bean.IPlayer;

/**
 * The Get Player By User Id Service.
 */
@GwtCompatible
@RemoteServiceRelativePath("GetPlayerByUserIdService")
public interface GetPlayerByUserIdService extends RemoteService, IGeneratedRemoteService {

    IPlayer getPlayerByUserId(long loginId, int instanceId, int id) throws GwtErrorCodeException;
}
