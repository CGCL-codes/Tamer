package com.plexobject.docusearch.jms;

import java.net.MalformedURLException;
import java.util.Set;
import javax.jms.ConnectionFactory;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.broker.region.policy.DeadLetterStrategy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.apache.activemq.broker.region.policy.StorePendingQueueMessageStoragePolicy;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.springframework.jms.core.JmsTemplate;

public abstract class BaseMessageTestCase {

    protected static final String CONNECTOR_URL = "tcp://localhost:61919";

    protected static final Logger LOGGER = Logger.getLogger(DeadLetterReprocessorTest.class);

    private static final long QUEUE_MEMORY_LIMIT = 2097152;

    protected static final String DEFAULT_BROKER = "test_broker";

    static {
        System.getProperties().setProperty("com.sun.management.jmxremote.port", "1099");
        System.getProperties().setProperty("com.sun.management.jmxremote.authenticate", "false");
        System.getProperties().setProperty("com.sun.management.jmxremote.ssl", "false");
    }

    protected final JmsTemplate jmsTemplate = new JmsTemplate();

    protected ConnectionFactory connectionFactory;

    protected ActiveMQConnection connection;

    protected BrokerService broker;

    @Before
    public void setUp() throws Exception {
        this.broker = createBroker();
        this.connectionFactory = createConnectionFactory();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setReceiveTimeout(200);
    }

    @After
    public void tearDown() throws Exception {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            LOGGER.error("failed to close connection " + e);
        }
        try {
            if (broker != null) {
                broker.stop();
            }
        } catch (Exception e) {
            LOGGER.error("failed to close broker " + e);
        }
    }

    private BrokerService createBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(true);
        broker.setDeleteAllMessagesOnStartup(true);
        broker.addConnector(CONNECTOR_URL);
        broker.setEnableStatistics(true);
        PolicyEntry policy = new PolicyEntry();
        policy.setMemoryLimit(QUEUE_MEMORY_LIMIT);
        policy.setPendingQueuePolicy(new StorePendingQueueMessageStoragePolicy());
        DeadLetterStrategy defaultDeadLetterStrategy = policy.getDeadLetterStrategy();
        if (defaultDeadLetterStrategy != null) {
            defaultDeadLetterStrategy.setProcessNonPersistent(true);
        }
        PolicyMap pMap = new PolicyMap();
        pMap.setDefaultEntry(policy);
        broker.setDestinationPolicy(pMap);
        ManagementContext managementContext = broker.getManagementContext();
        managementContext.setCreateConnector(true);
        managementContext.setCreateMBeanServer(true);
        managementContext.setRmiServerPort(1099);
        managementContext.setJmxDomainName("localhost");
        broker.setBrokerName(DEFAULT_BROKER);
        broker.start();
        return broker;
    }

    private ConnectionFactory createConnectionFactory() throws Exception {
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(CONNECTOR_URL);
        connectionFactory.setOptimizedMessageDispatch(true);
        connectionFactory.setCopyMessageOnSend(false);
        connectionFactory.setUseCompression(false);
        connectionFactory.setDispatchAsync(false);
        connectionFactory.setUseAsyncSend(false);
        connectionFactory.setOptimizeAcknowledge(false);
        connectionFactory.setWatchTopicAdvisories(true);
        ActiveMQPrefetchPolicy qPrefetchPolicy = new ActiveMQPrefetchPolicy();
        qPrefetchPolicy.setQueuePrefetch(100);
        qPrefetchPolicy.setTopicPrefetch(1000);
        connectionFactory.setPrefetchPolicy(qPrefetchPolicy);
        connectionFactory.setAlwaysSyncSend(true);
        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setMaximumRedeliveries(1);
        policy.setBackOffMultiplier((short) 1);
        policy.setInitialRedeliveryDelay(10);
        policy.setUseExponentialBackOff(false);
        connectionFactory.setRedeliveryPolicy(policy);
        connection = (ActiveMQConnection) connectionFactory.createConnection();
        connection.setClientID(toString());
        int rollbackCount = connection.getRedeliveryPolicy().getMaximumRedeliveries() + 1;
        LOGGER.debug("Will redeliver messages: " + rollbackCount + " times");
        connection.start();
        return connectionFactory;
    }

    protected void dumpJmxBeans() throws MalformedURLException {
        final JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
        try {
            JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
            Set<?> all = mbsc.queryMBeans(null, null);
            LOGGER.info("Total MBean count=" + all.size());
            for (Object o : all) {
                ObjectInstance bean = (ObjectInstance) o;
                LOGGER.info(bean.getObjectName());
            }
        } catch (Exception e) {
            LOGGER.warn("failed to connect " + e);
        }
    }
}
