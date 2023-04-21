package com.turnengine.client.global.user.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteService;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;

/**
 * The Set User Address Service.
 */
@GwtCompatible
@RemoteServiceRelativePath("SetUserAddressService")
public interface SetUserAddressService extends RemoteService, IGeneratedRemoteService {

    Boolean setUserAddress(int id, int address) throws GwtErrorCodeException;
}
