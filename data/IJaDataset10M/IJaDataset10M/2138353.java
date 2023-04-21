package net.taylor.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.NamingException;
import net.taylor.inject.Locator;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.util.Naming;

/**
 * It is preferable to use the Seam JMS components, but they depend on an active
 * context.
 * 
 * @author jgilbert01
 */
@Scope(ScopeType.EVENT)
@BypassInterceptors
@AutoCreate
public class JmsSender {

    private static final Log log = Logging.getLog(JmsSender.class);

    private String connectionFactoryJndiName = "java:/JmsXA";

    private String destinationJndiName;

    private boolean transacted = false;

    private int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;

    private int deliveryMode = DeliveryMode.PERSISTENT;

    private int priority = Message.DEFAULT_PRIORITY;

    private long timeToLive = Message.DEFAULT_TIME_TO_LIVE;

    private String user;

    private String credentials;

    public String getConnectionFactoryJndiName() {
        return connectionFactoryJndiName;
    }

    public void setConnectionFactoryJndiName(String connectionFactoryJndiName) {
        this.connectionFactoryJndiName = connectionFactoryJndiName;
    }

    public String getDestinationJndiName() {
        return destinationJndiName;
    }

    public void setDestinationJndiName(String destinationJndiName) {
        this.destinationJndiName = destinationJndiName;
    }

    public boolean isTransacted() {
        return transacted;
    }

    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    public int getAcknowledgeMode() {
        return acknowledgeMode;
    }

    public void setAcknowledgeMode(int acknowledgeMode) {
        this.acknowledgeMode = acknowledgeMode;
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    protected ConnectionFactory connectionFactory;

    protected Destination destination;

    protected Session session;

    protected Connection connection;

    protected MessageProducer producer;

    @Create
    public void create() throws NamingException, JMSException {
        log.debug("Creating: {0}", this);
        connectionFactory = (ConnectionFactory) Naming.getInitialContext().lookup(connectionFactoryJndiName);
        if (user != null && credentials != null) {
            log.debug("Using secured connection.");
            connection = connectionFactory.createConnection(user, credentials);
        } else {
            connection = connectionFactory.createConnection();
        }
        session = connection.createSession(transacted, acknowledgeMode);
        destination = (Destination) Naming.getInitialContext().lookup(destinationJndiName);
        producer = session.createProducer(destination);
    }

    public void send(MessageCallback callback) {
        try {
            Message message = callback.createMessage(session);
            log.debug("Sending message: {0}", message);
            producer.send(message, deliveryMode, priority, timeToLive);
            if (transacted) {
                session.commit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Destroy
    public void destroy() throws JMSException {
        log.debug("Destroy: {0}", this);
        if (producer != null) {
            producer.close();
        }
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public static JmsSender instance(String name) {
        return Locator.getInstance(name, JmsSender.class);
    }

    /**
	 * Use this when you do not have an active seam context. If you need to
	 * override more of the defaults then use this method as an example.
	 */
    public static void unmanagedSend(String destinationJndiName, MessageCallback callback) {
        JmsSender sender = new JmsSender();
        sender.setDestinationJndiName(destinationJndiName);
        try {
            sender.create();
            sender.send(callback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                sender.destroy();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
