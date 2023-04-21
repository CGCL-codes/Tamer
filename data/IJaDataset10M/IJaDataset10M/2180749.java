package com.turnengine.client.local.unit.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteService;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;
import com.turnengine.client.local.unit.bean.IUnit;
import java.util.List;

/**
 * The Get Units Service.
 */
@GwtCompatible
@RemoteServiceRelativePath("GetUnitsService")
public interface GetUnitsService extends RemoteService, IGeneratedRemoteService {

    List<IUnit> getUnits(long loginId, int instanceId) throws GwtErrorCodeException;
}
