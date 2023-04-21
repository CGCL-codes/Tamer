package net.sf.phoenixjms.mds.impl;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationSerializer;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.activation.lifecycle.LifecycleCreateExtension;
import org.apache.avalon.activation.lifecycle.LifecycleDestroyExtension;
import org.apache.avalon.meta.info.StageDescriptor;
import org.apache.avalon.composition.model.DeploymentModel;
import org.apache.commons.messenger.Messenger;
import org.apache.commons.messenger.MessengerDigester;
import org.apache.commons.messenger.MessengerManager;
import org.apache.commons.messagelet.ConsumerThread;
import org.apache.commons.messagelet.SubscriptionManager;
import org.apache.commons.messagelet.model.Subscription;
import org.apache.commons.messagelet.model.SubscriptionList;
import net.sf.phoenixjms.mds.MDSManager;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.io.StringReader;
import javax.jms.MessageListener;
import org.xml.sax.*;
import java.io.*;
import javax.servlet.*;
import javax.jms.*;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.context.ContextException;

/**
 * Default Message Driven Manager Implementation.
 * <pre>
 *   URNS:
 *     urn:mds:listener   =  avalon stage extension name
 *     urn:mds:connection =  messenger name
 *     urn:mds:subject    =  topic or queue name
 *     urn:mds:selector   =  optional message selector
 * </pre>
 * Alternatively, MessageListeners can include this line in their Avalon
 * configuration:
 * <pre>
      &lt;subscriber connection="messengerName" subject="topicOrQueueName" selector="optional"/&gt;
 * </pre>
 * @avalon.component  name="mds-manager" version="1.0" lifestyle="singleton"
 * @avalon.service type="net.sf.phoenixjms.mds.MDSManager" version="1.0"
 * @avalon.extension id="urn:mds:listener"
 */
public class DefaultMDSManager extends AbstractLogEnabled implements Contextualizable, Serviceable, Configurable, Initializable, Startable, MDSManager, LifecycleCreateExtension, LifecycleDestroyExtension {

    protected ServiceManager m_serviceManager;

    protected MessengerManager m_msgManager;

    protected List m_subscribers;

    protected Hashtable m_subscriberTable = new Hashtable();

    protected SubscriptionManager m_subManager;

    protected String m_baseDir = System.getProperty("maven.home", System.getProperty("user.dir"));

    protected long m_delay = 0;

    public DefaultMDSManager() {
    }

    public Iterator getMessengerNames() {
        return m_msgManager.getMessengerNames();
    }

    public Messenger getMessenger(String name) throws JMSException {
        return m_msgManager.getMessenger(name);
    }

    public void create(DeploymentModel deploymentModel, StageDescriptor stageDescriptor, Object object) throws Exception {
        if (object instanceof MessageListener) {
            Integer hash = new Integer(object.hashCode());
            if (m_subscriberTable.containsKey(hash) == false) {
                Configuration c = deploymentModel.getConfiguration();
                if (c == null) c = new DefaultConfiguration("configuration");
                Configuration s = c.getChild("subscriber", true);
                String connection = s.getAttribute("connection", stageDescriptor.getAttribute("urn:mds:connection", null));
                String subject = s.getAttribute("subject", stageDescriptor.getAttribute("urn:mds:subject", null));
                String selector = s.getAttribute("selector", stageDescriptor.getAttribute("urn:mds:selector", null));
                Subscription sub = new Subscription();
                sub.setMessageListener((MessageListener) object);
                sub.setConnection(connection);
                sub.setSubject(subject);
                sub.setSelector(selector);
                sub.setConsumerThread(new ConsumerThread());
                m_subscriberTable.put(hash, sub);
                m_subscribers.add(sub);
                m_subManager.subscribe(sub);
                if (getLogger().isDebugEnabled()) getLogger().debug("subscribed: " + deploymentModel.getName() + " to " + connection + ":" + subject);
            }
        }
    }

    public void destroy(DeploymentModel deploymentModel, StageDescriptor stageDescriptor, Object object) {
        if (object instanceof MessageListener) {
            Integer hash = new Integer(object.hashCode());
            if (m_subscriberTable.containsKey(hash)) {
                try {
                    Subscription sub = (Subscription) m_subscriberTable.get(hash);
                    m_subManager.unsubscribe(sub);
                    m_subscriberTable.remove(hash);
                } catch (JMSException ex) {
                    getLogger().error("", ex);
                } catch (ServletException ex) {
                    getLogger().error("", ex);
                }
            }
        }
    }

    /**
   * @avalon.entry key="urn:avalon:home" type="java.io.File"
   * @param context
   */
    public void contextualize(Context context) {
        try {
            File home = (File) context.get("urn:avalon:home");
            if (home != null) {
                m_baseDir = home.getAbsolutePath();
            }
        } catch (ContextException ex) {
            if (getLogger().isWarnEnabled()) getLogger().warn("Failed to get 'urn:avalon:home' from context.  Using default: " + m_baseDir);
        }
    }

    public void service(ServiceManager manager) throws ServiceException {
        m_serviceManager = manager;
    }

    /**
   * configure the MessageManager.
   * Valid configuration:
   * <pre>
   *    &lt;configuration&gt;
   *       &lt;messenger-conf&gt;../apps/messenger/Messenger.xml&lt;/messenger-conf&gt;
   *    &lt;/configuration&gt;
   * </pre>
   * Note: there is a second configuration option to include the contents of Messenger.xml
   * in the DefaultMDSManager configuration. However currently (v.0.1) this is
   * buggy.  Recommend using external configuration.
   * @param conf Avalon Configuration
   * @throws ConfigurationException
   */
    public void configure(Configuration conf) throws ConfigurationException {
        try {
            Configuration c = conf.getChild("messenger-conf", true);
            String confFile = c.getValue(null);
            long delay = Long.parseLong(conf.getChild("delay", true).getValue("0"));
            getLogger().info("delay = " + delay);
            if (delay > 0) m_delay = delay;
            if (m_delay > 0) {
                if (getLogger().isDebugEnabled()) getLogger().debug("Delaying deployment of MDS Manager by " + m_delay + " ms");
                Thread.sleep(m_delay);
            }
            if (confFile != null) {
                if (getLogger().isDebugEnabled()) getLogger().debug("configuring messenger manager via: " + m_baseDir + confFile);
                m_msgManager = MessengerManager.load(m_baseDir + confFile);
            } else {
                if (getLogger().isDebugEnabled()) getLogger().debug("serializing messengers configuration");
                DefaultConfigurationSerializer serializer = new DefaultConfigurationSerializer();
                Configuration messengers = conf.getChild("messengers", true);
                StringReader reader = new StringReader(serializer.serialize(messengers));
                MessengerDigester digester = new MessengerDigester();
                m_msgManager = (MessengerManager) digester.parse(reader);
            }
            Configuration subscribers = conf.getChild("subscribers", true);
            m_subscribers = getSubscribers(subscribers);
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Configured Messengers: ");
                Iterator it = m_msgManager.getMessengerNames();
                while (it.hasNext()) {
                    String name = (String) it.next();
                    getLogger().debug("messenger: " + name);
                }
            }
        } catch (Exception e) {
            getLogger().error("Exception configuring MessengerManager", e);
            throw new ConfigurationException("Exception configuring MDSManager", e);
        }
    }

    public void initialize() throws Exception {
        m_subManager = new SubscriptionManager();
        m_subManager.setMessengerManager(m_msgManager);
    }

    public void start() {
        for (int i = 0; i < m_subscribers.size(); i++) {
            try {
                Subscription sub = (Subscription) m_subscribers.get(i);
                m_subManager.subscribe(sub);
            } catch (ServletException ex) {
                getLogger().error("", ex);
            } catch (JMSException ex) {
                getLogger().error("", ex);
            }
        }
    }

    public void stop() {
        for (int i = 0; i < m_subscribers.size(); i++) {
            try {
                Subscription sub = (Subscription) m_subscribers.get(i);
                m_subManager.unsubscribe(sub);
            } catch (ServletException ex) {
                getLogger().error("", ex);
            } catch (JMSException ex) {
                getLogger().error("", ex);
            }
        }
    }

    /**
   * Valid Configuration scheme:
      &lt;subscriber connection="messengerName" subject="topicOrQueueName" selector="optional"&gt;
         &lt;service name="avalonServiceName"/&gt;
      &lt;/subscriber&gt;
   */
    protected List getSubscribers(Configuration conf) throws ConfigurationException {
        List list = new ArrayList();
        Configuration[] subscribers = conf.getChildren("subscription");
        for (int i = 0; i < subscribers.length; i++) {
            Configuration sub = subscribers[i];
            Subscription subscriber = new Subscription();
            subscriber.setConnection(sub.getAttribute("connection"));
            subscriber.setSubject(sub.getAttribute("subject"));
            if (sub.getAttribute("selector", null) != null) {
                subscriber.setSelector(sub.getAttribute("selector"));
            }
            Configuration[] clients = sub.getChildren();
            for (int j = 0; j < clients.length; j++) {
                Configuration client = clients[j];
                if (client.getName().equalsIgnoreCase("service")) {
                    String name = client.getAttribute("name");
                    if (m_serviceManager.hasService(name)) {
                        try {
                            MessageListener listener = (MessageListener) m_serviceManager.lookup(name);
                            subscriber.setMessageListener(listener);
                            subscriber.setConsumerThread(new ConsumerThread());
                            list.add(subscriber);
                        } catch (ServiceException ex) {
                            getLogger().error("", ex);
                        }
                    }
                }
            }
        }
        return list;
    }
}
