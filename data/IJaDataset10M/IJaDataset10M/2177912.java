package com.turnengine.client.global.game.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteService;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;
import com.turnengine.client.global.game.bean.IGameHost;

/**
 * The New Game Host Service.
 */
@GwtCompatible
@RemoteServiceRelativePath("NewGameHostService")
public interface NewGameHostService extends RemoteService, IGeneratedRemoteService {

    IGameHost newGameHost(long loginId, String name, int port) throws GwtErrorCodeException;
}
