package com.turnengine.client.local.unit.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteService;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;

/**
 * The Get Unit Score Service.
 */
@GwtCompatible
@RemoteServiceRelativePath("GetUnitScoreService")
public interface GetUnitScoreService extends RemoteService, IGeneratedRemoteService {

    long getUnitScore(long loginId, int instanceId, String name, String groupName) throws GwtErrorCodeException;
}
