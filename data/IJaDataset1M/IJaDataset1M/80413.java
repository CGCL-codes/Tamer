package org.openxava.test.model;

import org.openxava.ejbx.*;

/**
 * Utility class for Invoice.
 */
public class InvoiceUtil {

    /** Cached remote home (EJBHome). Uses lazy loading to obtain its value (loaded by getHome() methods). */
    private static org.openxava.test.model.InvoiceHome cachedRemoteHome = null;

    private static Object lookupHome(java.util.Hashtable environment, String jndiName, Class narrowTo) throws javax.naming.NamingException {
        IContext initialContext = BeansContext.get();
        try {
            Object objRef = initialContext.lookup(jndiName);
            return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo);
        } finally {
            initialContext.close();
        }
    }

    /**
    * Obtain remote home interface from default initial context
    * @return Home interface for Invoice. Lookup using JNDI_NAME
    */
    public static org.openxava.test.model.InvoiceHome getHome() throws javax.naming.NamingException {
        if (cachedRemoteHome == null) {
            cachedRemoteHome = (org.openxava.test.model.InvoiceHome) lookupHome(null, org.openxava.test.model.InvoiceHome.JNDI_NAME, org.openxava.test.model.InvoiceHome.class);
        }
        return cachedRemoteHome;
    }

    /**
    * Obtain remote home interface from parameterised initial context
    * @param environment Parameters to use for creating initial context
    * @return Home interface for Invoice. Lookup using JNDI_NAME
    */
    public static org.openxava.test.model.InvoiceHome getHome(java.util.Hashtable environment) throws javax.naming.NamingException {
        return (org.openxava.test.model.InvoiceHome) lookupHome(environment, org.openxava.test.model.InvoiceHome.JNDI_NAME, org.openxava.test.model.InvoiceHome.class);
    }
}
