package org.readyesb.web.util;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.ejb.EJBHome;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RemoteEJBMethods {

    private static Log log = LogFactory.getLog(RemoteEJBMethods.class);

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }

    public Object propertiesMethod(String JNDIName) {
        Object ejbHome = null;
        try {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            properties.put(Context.PROVIDER_URL, "t3://133.128.9.19:9001,133.128.9.20:9001");
            Context context = new InitialContext(properties);
            Object ref = context.lookup(JNDIName);
            ejbHome = PortableRemoteObject.narrow(ref, EJBHome.class);
        } catch (NamingException ne) {
            log.error(ne);
        } finally {
        }
        return ejbHome;
    }
}
