package com.turnengine.client.local.unit.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteService;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;
import com.turnengine.client.local.unit.bean.IStorageGroup;

/**
 * The Set Storage Group Service.
 */
@GwtCompatible
@RemoteServiceRelativePath("SetStorageGroupService")
public interface SetStorageGroupService extends RemoteService, IGeneratedRemoteService {

    Boolean setStorageGroup(long loginId, int instanceId, IStorageGroup group) throws GwtErrorCodeException;
}
