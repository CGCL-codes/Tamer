package org.mobicents.xdm.server.appusage.oma.prescontent;

import java.io.IOException;
import java.util.Hashtable;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.mobicents.slee.enabler.userprofile.jpa.jmx.UserProfileControlManagementMBean;

public class AbstractT {

    private ObjectName userProfileMBeanObjectName;

    private RMIAdaptor rmiAdaptor;

    protected void initRmiAdaptor() throws NamingException, MalformedObjectNameException, NullPointerException {
        Hashtable env = new Hashtable();
        env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");
        InitialContext ctx = new InitialContext(env);
        rmiAdaptor = (RMIAdaptor) ctx.lookup("jmx/rmi/RMIAdaptor");
        userProfileMBeanObjectName = new ObjectName(UserProfileControlManagementMBean.MBEAN_NAME);
    }

    protected void createUser(String user, String password) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
        String sigs[] = { String.class.getName(), String.class.getName() };
        Object[] args = { user, password };
        rmiAdaptor.invoke(userProfileMBeanObjectName, "addUser", args, sigs);
    }

    protected void removeUser(String user) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
        String sigs[] = { String.class.getName() };
        Object[] args = { user };
        rmiAdaptor.invoke(userProfileMBeanObjectName, "removeUser", args, sigs);
    }
}
