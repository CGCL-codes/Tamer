package org.apache.axis.components.jms;

import java.util.HashMap;
import java.util.Hashtable;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.axis.transport.jms.JMSConstants;
import org.apache.axis.transport.jms.JMSURLHelper;

/**
 * Uses JNDI to locate ConnectionFactory and Destinations
 *
 * @author Jaime Meritt (jmeritt@sonicsoftware.com)
 * @author Ray Chun (rchun@sonicsoftware.com)
 */
public class JNDIVendorAdapter extends JMSVendorAdapter {

    public static final String CONTEXT_FACTORY = "java.naming.factory.initial";

    public static final String PROVIDER_URL = "java.naming.provider.url";

    public static final String _CONNECTION_FACTORY_JNDI_NAME = "ConnectionFactoryJNDIName";

    public static final String CONNECTION_FACTORY_JNDI_NAME = JMSConstants.JMS_PROPERTY_PREFIX + _CONNECTION_FACTORY_JNDI_NAME;

    private Context context;

    public QueueConnectionFactory getQueueConnectionFactory(HashMap cfConfig) throws Exception {
        return (QueueConnectionFactory) getConnectionFactory(cfConfig);
    }

    public TopicConnectionFactory getTopicConnectionFactory(HashMap cfConfig) throws Exception {
        return (TopicConnectionFactory) getConnectionFactory(cfConfig);
    }

    private ConnectionFactory getConnectionFactory(HashMap cfProps) throws Exception {
        if (cfProps == null) throw new IllegalArgumentException("noCFProps");
        String jndiName = (String) cfProps.get(CONNECTION_FACTORY_JNDI_NAME);
        if (jndiName == null || jndiName.trim().length() == 0) throw new IllegalArgumentException("noCFName");
        Hashtable environment = new Hashtable(cfProps);
        String ctxFactory = (String) cfProps.get(CONTEXT_FACTORY);
        if (ctxFactory != null) environment.put(CONTEXT_FACTORY, ctxFactory);
        String providerURL = (String) cfProps.get(PROVIDER_URL);
        if (providerURL != null) environment.put(PROVIDER_URL, providerURL);
        context = new InitialContext(environment);
        return (ConnectionFactory) context.lookup(jndiName);
    }

    /**
     * Populates the connection factory config table with properties from
     * the JMS URL query string
     *
     * @param jmsurl The target endpoint address of the Axis call
     * @param cfConfig The set of properties necessary to create/configure the connection factory
     */
    public void addVendorConnectionFactoryProperties(JMSURLHelper jmsurl, HashMap cfConfig) {
        String cfJNDIName = jmsurl.getPropertyValue(_CONNECTION_FACTORY_JNDI_NAME);
        if (cfJNDIName != null) cfConfig.put(CONNECTION_FACTORY_JNDI_NAME, cfJNDIName);
        String ctxFactory = jmsurl.getPropertyValue(CONTEXT_FACTORY);
        if (ctxFactory != null) cfConfig.put(CONTEXT_FACTORY, ctxFactory);
        String providerURL = jmsurl.getPropertyValue(PROVIDER_URL);
        if (providerURL != null) cfConfig.put(PROVIDER_URL, providerURL);
    }

    /**
     * Check that the attributes of the candidate connection factory match the
     * requested connection factory properties.
     *
     * @param cf the candidate connection factory
     * @param originalJMSURL the URL which was used to create the connection factory
     * @param cfProps the set of properties that should be used to determine the match
     * @return true or false to indicate whether a match has been found
     */
    public boolean isMatchingConnectionFactory(ConnectionFactory cf, JMSURLHelper originalJMSURL, HashMap cfProps) {
        JMSURLHelper jmsurl = (JMSURLHelper) cfProps.get(JMSConstants.JMS_URL);
        String cfJndiName = jmsurl.getPropertyValue(_CONNECTION_FACTORY_JNDI_NAME);
        String originalCfJndiName = originalJMSURL.getPropertyValue(_CONNECTION_FACTORY_JNDI_NAME);
        if (cfJndiName.equalsIgnoreCase(originalCfJndiName)) return true;
        return false;
    }

    public Queue getQueue(QueueSession session, String name) throws Exception {
        return (Queue) context.lookup(name);
    }

    public Topic getTopic(TopicSession session, String name) throws Exception {
        return (Topic) context.lookup(name);
    }
}
