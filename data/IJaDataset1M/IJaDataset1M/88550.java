package com.google.solarchallenge.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.solarchallenge.shared.dtos.UserAccountDto;

/**
 * GWT RPC service for Login.
 *
 * @author Arjun Satyapal
 */
@RemoteServiceRelativePath("loginService")
public interface LoginService extends RemoteService {

    /**
   * Get details of Logged in User.
   */
    UserAccountDto getLoggedInUserDTO();
}
