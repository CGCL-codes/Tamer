package com.carbonfive.flashgateway.security;

import java.util.*;
import javax.servlet.http.*;
import com.carbonfive.flashgateway.security.config.*;
import org.apache.commons.logging.*;

/**
 * Gatekeeper enforces service invocation rules based on the FlashGatekeeper configuration.
 */
public class Gatekeeper {

    private static Log log = LogFactory.getLog(Gatekeeper.class.getName());

    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public boolean canInvoke(HttpServletRequest request, String serviceName, String methodName) {
        if (log.isDebugEnabled()) {
            log.debug("Checking access for service " + serviceName + ", method " + methodName + " by user principal " + request.getUserPrincipal());
        }
        Service service = config.getService(serviceName);
        if (service == null) return false;
        Method method = service.getMethod(methodName);
        if (method == null) return false;
        if (method.getConstraint() == null) return true;
        return userIsInRole(request, method.getConstraint());
    }

    private boolean userIsInRole(HttpServletRequest request, AccessConstraint constraint) {
        for (Iterator i = constraint.getRoleNames().iterator(); i.hasNext(); ) {
            if (request.isUserInRole((String) i.next())) return true;
        }
        return false;
    }
}
