package org.openxava.test.ejb;

import org.openxava.ejbx.*;

/**
 * Utility class for Subfamily2.
 */
public class Subfamily2Util {

    /** Cached remote home (EJBHome). Uses lazy loading to obtain its value (loaded by getHome() methods). */
    private static org.openxava.test.ejb.Subfamily2Home cachedRemoteHome = null;

    private static Object lookupHome(java.util.Hashtable environment, String jndiName, Class narrowTo) throws javax.naming.NamingException {
        IContext initialContext = BeansContext.get();
        try {
            Object objRef = initialContext.lookup(jndiName);
            if (narrowTo.isInstance(java.rmi.Remote.class)) return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo); else return objRef;
        } finally {
            initialContext.close();
        }
    }

    /**
    * Obtain remote home interface from default initial context
    * @return Home interface for Subfamily2. Lookup using JNDI_NAME
    */
    public static org.openxava.test.ejb.Subfamily2Home getHome() throws javax.naming.NamingException {
        if (cachedRemoteHome == null) {
            cachedRemoteHome = (org.openxava.test.ejb.Subfamily2Home) lookupHome(null, org.openxava.test.ejb.Subfamily2Home.JNDI_NAME, org.openxava.test.ejb.Subfamily2Home.class);
        }
        return cachedRemoteHome;
    }

    /**
    * Obtain remote home interface from parameterised initial context
    * @param environment Parameters to use for creating initial context
    * @return Home interface for Subfamily2. Lookup using JNDI_NAME
    */
    public static org.openxava.test.ejb.Subfamily2Home getHome(java.util.Hashtable environment) throws javax.naming.NamingException {
        return (org.openxava.test.ejb.Subfamily2Home) lookupHome(environment, org.openxava.test.ejb.Subfamily2Home.JNDI_NAME, org.openxava.test.ejb.Subfamily2Home.class);
    }
}
