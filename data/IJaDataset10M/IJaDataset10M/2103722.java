package com.j2xtreme.xbean;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * InitialContextFactory implementation that allows XBean services
 * to be resolved using JNDI.
 * @see javax.naming.spi.InitialContextFactory
 * @author Rob Schoening
 * @version $Id: InitialContextFactory.java,v 1.4 2004/11/08 05:23:35 rschoening Exp $
 */
public class InitialContextFactory implements javax.naming.spi.InitialContextFactory {

    public Context getInitialContext(Hashtable env) throws NamingException {
        return new XBeanContext(env);
    }
}
