package org.go.ee.jmx.middle.localhost;

import java.util.Arrays;
import java.util.Properties;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.go.ee.jmx.RemoteMBeanScheduler;
import org.go.expcetion.SchedulerException;

/**
 * <p>
 * An implementation of the <code>Scheduler</code> interface that remotely
 * proxies all method calls to the equivalent call on a given <code>Scheduler</code>
 * instance, via JBoss's JMX RMIAdaptor.
 * </p>
 * 
 * <p>
 * Set the <b>providerURL</b> property to your MBean server URL. 
 * This defaults to: <i>jnp://localhost:1099</i>
 * </p>
 * 
 * @see org.go.Scheduler
 * @see org.go.core.MemoryScheduler
 * @see org.go.core.SchedulingContext
 */
public class RmiRemoteMBeanScheduler extends RemoteMBeanScheduler {

    private static final String DEFAULT_PROVIDER_URL = "jnp://localhost:1099";

    private static final String RMI_ADAPTOR_JNDI_NAME = "jmx/rmi/RMIAdaptor";

    private MBeanServerConnection server = null;

    private String providerURL = DEFAULT_PROVIDER_URL;

    public RmiRemoteMBeanScheduler() throws SchedulerException {
    }

    /**
	 * Set the remote MBean server URL.  
	 * 
	 * Defaults to: <i>jnp://localhost:1099</i>
	 */
    public void setProviderURL(String providerURL) {
        this.providerURL = providerURL;
    }

    /**
	 * Initialize this remote MBean scheduler, getting the JBoss
	 * RMIAdaptor for communication.
	 */
    public void initialize() throws SchedulerException {
        InitialContext ctx = null;
        try {
            ctx = new InitialContext(getContextProperties());
            server = (MBeanServerConnection) ctx.lookup(RMI_ADAPTOR_JNDI_NAME);
        } catch (Exception e) {
            throw new SchedulerException("Failed to lookup JBoss JMX RMI Adaptor.", e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ignore) {
                }
            }
        }
    }

    /**
	 * Get the properties to use when creating a JNDI InitialContext.
	 * 
	 * <p>
	 * This method is broken out so it can be extended to pass credentials
	 * or other properties not currently supported. 
	 * </p>
	 */
    protected Properties getContextProperties() {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
        props.put(Context.PROVIDER_URL, providerURL);
        return props;
    }

    @Override
    protected Object getAttribute(String attribute) throws SchedulerException {
        try {
            return server.getAttribute(getSchedulerObjectName(), attribute);
        } catch (Exception e) {
            throw new SchedulerException("Failed to get Scheduler MBean attribute: " + attribute, e);
        }
    }

    @Override
    protected AttributeList getAttributes(String[] attributes) throws SchedulerException {
        try {
            return server.getAttributes(getSchedulerObjectName(), attributes);
        } catch (Exception e) {
            throw new SchedulerException("Failed to get Scheduler MBean attributes: " + Arrays.asList(attributes), e);
        }
    }

    @Override
    protected Object invoke(String operationName, Object[] params, String[] signature) throws SchedulerException {
        try {
            return server.invoke(getSchedulerObjectName(), operationName, params, signature);
        } catch (Exception e) {
            throw new SchedulerException("Failed to invoke Scheduler MBean operation: " + operationName, e);
        }
    }

    @Override
    public String getVersion() {
        return null;
    }
}
