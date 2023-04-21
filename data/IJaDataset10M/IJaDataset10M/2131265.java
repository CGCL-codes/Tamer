package com.ixora.wls;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author Daniel Moraru
 */
public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            try {
                JMXConnector fConnector;
                MBeanServerConnection fMBeanServer;
                Map<String, Object> fNamingEnv;
                fNamingEnv = new HashMap<String, Object>();
                fNamingEnv.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
                fNamingEnv.put(JMXConnectorServerFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
                fNamingEnv.put(Context.SECURITY_PRINCIPAL, "weblogic");
                fNamingEnv.put(Context.SECURITY_CREDENTIALS, "weblogic");
                fNamingEnv.put(Context.PROVIDER_URL, "t3://localhost:7001");
                JMXServiceURL url = new JMXServiceURL("service:jmx:t3://localhost:7001/jndi/weblogic.management.adminhome");
                fConnector = JMXConnectorFactory.connect(url, fNamingEnv);
                fMBeanServer = fConnector.getMBeanServerConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Hashtable props = new Hashtable();
            props.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
            props.put("java.naming.provider.url", "t3://localhost:7001");
            props.put("java.naming.security.principal", "weblogic");
            props.put("java.naming.security.credentials", "weblogic");
            Context ctx = new InitialContext(props);
            Object home = ctx.lookup("weblogic.management.adminhome");
            Class homeClass = home.getClass();
            Method method = homeClass.getMethod("getMBeanServer", new Class[0]);
            Object mbeanServerObject = method.invoke(home, new Object[0]);
            MBeanServer server = (MBeanServer) mbeanServerObject;
            int i = 0;
            System.out.println(server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
