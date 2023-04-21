package com.windsor.node.common.service.admin;

import com.windsor.node.common.domain.NodeVisit;
import com.windsor.node.common.domain.AuthenticationRequest;

public interface AdminSecurityService {

    /**
     * authenticate
     * 
     * @param credentials
     * @param requestedFromIp
     * @return
     */
    NodeVisit authenticate(AuthenticationRequest credentials, String requestedFromIp);
}
