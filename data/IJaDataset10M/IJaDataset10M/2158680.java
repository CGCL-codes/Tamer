package com.turnengine.client.global.user.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteService;
import com.javabi.command.errorcode.gwt.GwtErrorCodeException;

/**
 * The Verify User Service.
 */
@GwtCompatible
@RemoteServiceRelativePath("VerifyUserService")
public interface VerifyUserService extends RemoteService, IGeneratedRemoteService {

    Boolean verifyUser(int id, long code, int address) throws GwtErrorCodeException;
}
