package org.jrest4guice.security;

import org.jrest4guice.guice.GuiceContext;
import com.google.inject.Provider;

public class SecurityContextProvoider implements Provider<SecurityContext> {

    private static ThreadLocal<SecurityContext> securityContext = new ThreadLocal<SecurityContext>();

    @Override
    public SecurityContext get() {
        SecurityContext context = securityContext.get();
        if (context == null) {
            context = new SecurityContext();
            GuiceContext.getInstance().injectorMembers(context);
            securityContext.set(context);
        }
        return context;
    }

    public static void clearCurrentSecurityContext() {
        securityContext.remove();
    }
}
