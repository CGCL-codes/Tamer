package org.plazmaforge.bsolution.base;

import org.plazmaforge.framework.platform.Environment;

/**
 * 
 * @author Oleh Hapon
 * $Id: SessionEnvironment.java,v 1.3 2010/04/28 06:24:25 ohapon Exp $
 */
public class SessionEnvironment extends Environment {

    private static SessionContext context;

    private SessionEnvironment() {
        super();
    }

    public static SessionContext getContext() {
        return context;
    }

    public static void setContext(SessionContext context) {
        SessionEnvironment.context = context;
    }
}
