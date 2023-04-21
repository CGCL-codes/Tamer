package com.turnengine.client.local.turn.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteService;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;

/**
 * The Get Turn Number Service.
 */
@GwtCompatible
@RemoteServiceRelativePath("GetTurnNumberService")
public interface GetTurnNumberService extends RemoteService, IGeneratedRemoteService {

    int getTurnNumber(long loginId, int instanceId) throws GwtErrorCodeException;
}
