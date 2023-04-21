package com.turnengine.client.local.location.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteService;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;

/**
 * The Set Coordinate Grid Plugin Service.
 */
@GwtCompatible
@RemoteServiceRelativePath("SetCoordinateGridPluginService")
public interface SetCoordinateGridPluginService extends RemoteService, IGeneratedRemoteService {

    Boolean setCoordinateGridPlugin(long loginId, int instanceId, String plugin) throws GwtErrorCodeException;
}
