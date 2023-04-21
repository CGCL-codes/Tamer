package com.turnengine.client.global.game.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteService;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;
import com.turnengine.client.global.game.bean.IGameDefinition;
import java.util.List;

/**
 * The Get Game Definitions Service.
 */
@GwtCompatible
@RemoteServiceRelativePath("GetGameDefinitionsService")
public interface GetGameDefinitionsService extends RemoteService, IGeneratedRemoteService {

    List<IGameDefinition> getGameDefinitions() throws GwtErrorCodeException;
}
