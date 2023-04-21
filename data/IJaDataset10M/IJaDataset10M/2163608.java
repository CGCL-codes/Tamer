package com.wgo.bpot.common.transport.servicefacade;

import com.wgo.bpot.common.transport.exception.InvalidUserSessionException;
import com.wgo.bpot.domain.common.Service;
import com.wgo.bpot.domain.common.UserCredential;
import com.wgo.bpot.domain.common.UserSession;

public interface RemoteServices {

    public UserSession authenticateUser(UserCredential userCredential);

    public Object invokeService(UserSession userSession, Service service, Object[] args) throws InvalidUserSessionException;
}
