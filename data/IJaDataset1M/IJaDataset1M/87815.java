package jaxlib.ee.jms;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import jaxlib.ee.transaction.TransactionContext;
import jaxlib.util.CheckArg;

/**
 * Simplifies working with <i>JMS</i> connections.
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: JmsConnection.java 3048 2012-02-06 11:16:09Z joerg_wassmer $
 */
public class JmsConnection extends Object implements AutoCloseable {

    private Connection connection;

    private ConnectionFactory connectionFactory;

    private MessageConsumer consumer;

    private Destination destination;

    private MessageProducer producer;

    private Session session;

    private final int acknowledgeMode;

    private final boolean commit;

    private final boolean transactional;

    private JmsDestinationRef destinationRef;

    public JmsConnection(final ConnectionFactory connectionFactory, final Destination destination) {
        super();
        CheckArg.notNull(connectionFactory, "connectionFactory");
        CheckArg.notNull(destination, "destination");
        this.connectionFactory = connectionFactory;
        this.destination = destination;
        if (TransactionContext.current() == null) {
            this.acknowledgeMode = Session.AUTO_ACKNOWLEDGE;
            this.commit = true;
            this.transactional = true;
        } else {
            this.acknowledgeMode = Session.SESSION_TRANSACTED;
            this.commit = false;
            this.transactional = false;
        }
    }

    public JmsConnection(final ConnectionFactory connectionFactory, final Destination destination, final boolean transactional, final int acknowledgeMode) {
        super();
        CheckArg.notNull(connectionFactory, "connectionFactory");
        CheckArg.notNull(destination, "destination");
        this.acknowledgeMode = acknowledgeMode;
        this.commit = true;
        this.connectionFactory = connectionFactory;
        this.destination = destination;
        this.transactional = transactional;
    }

    public JmsConnection(final ConnectionFactory connectionFactory, final JmsDestinationRef destinationRef) {
        super();
        CheckArg.notNull(connectionFactory, "connectionFactory");
        CheckArg.notNull(destinationRef, "destinationRef");
        this.connectionFactory = connectionFactory;
        this.destinationRef = destinationRef;
        if (TransactionContext.current() == null) {
            this.acknowledgeMode = Session.AUTO_ACKNOWLEDGE;
            this.commit = true;
            this.transactional = true;
        } else {
            this.acknowledgeMode = Session.SESSION_TRANSACTED;
            this.commit = false;
            this.transactional = false;
        }
    }

    public JmsConnection(final ConnectionFactory connectionFactory, final JmsDestinationRef destinationRef, final boolean transactional, final int acknowledgeMode) {
        super();
        CheckArg.notNull(connectionFactory, "connectionFactory");
        CheckArg.notNull(destinationRef, "destinationRef");
        this.acknowledgeMode = acknowledgeMode;
        this.commit = true;
        this.connectionFactory = connectionFactory;
        this.destinationRef = destinationRef;
        this.transactional = transactional;
    }

    @Override
    public void close() throws JMSException {
        if (this.connectionFactory != null) {
            final Connection connection = this.connection;
            try {
                closeSession();
            } finally {
                this.connection = null;
                this.connectionFactory = null;
                this.consumer = null;
                this.destination = null;
                this.producer = null;
                this.session = null;
                if (connection != null) connection.close();
            }
        }
    }

    public void closeSession() throws JMSException {
        final Session session = this.session;
        if (session != null) {
            this.consumer = null;
            this.producer = null;
            this.session = null;
            session.close();
        }
    }

    public void commit() throws JMSException {
        if (this.commit) {
            final Session session = getSession();
            if (session.getTransacted()) session.commit();
        }
    }

    @Nonnull
    @CheckReturnValue
    public QueueBrowser createBrowser() throws JMSException {
        return createBrowser(null);
    }

    @Nonnull
    @CheckReturnValue
    public QueueBrowser createBrowser(@Nullable final String selector) throws JMSException {
        final Destination d = getDestination();
        if (!(d instanceof Queue)) throw new javax.jms.IllegalStateException("destination is not a queue: " + d);
        return getSession().createBrowser((Queue) d, selector);
    }

    @Nonnull
    @CheckReturnValue
    public BytesMessage createBytesMessage() throws JMSException {
        return getSession().createBytesMessage();
    }

    @Nonnull
    @CheckReturnValue
    public BytesMessage createBytesMessage(@Nullable final byte[] bytes) throws JMSException {
        final BytesMessage msg = createBytesMessage();
        if (bytes != null) msg.writeBytes(bytes);
        return msg;
    }

    @Nonnull
    @CheckReturnValue
    public MessageConsumer createConsumer(@Nullable final String selector) throws JMSException {
        return getSession().createConsumer(getDestination(), selector);
    }

    @Nonnull
    @CheckReturnValue
    public MapMessage createMapMessage() throws JMSException {
        return getSession().createMapMessage();
    }

    @Nonnull
    @CheckReturnValue
    public Message createMessage() throws JMSException {
        return getSession().createMessage();
    }

    @Nonnull
    @CheckReturnValue
    public ObjectMessage createObjectMessage() throws JMSException {
        return getSession().createObjectMessage();
    }

    @Nonnull
    @CheckReturnValue
    public StreamMessage createStreamMessage() throws JMSException {
        return getSession().createStreamMessage();
    }

    @Nonnull
    @CheckReturnValue
    public TextMessage createTextMessage() throws JMSException {
        return getSession().createTextMessage();
    }

    @Nonnull
    @CheckReturnValue
    public TextMessage createTextMessage(@Nullable final String text) throws JMSException {
        final TextMessage msg = createTextMessage();
        if (text != null) msg.setText(text);
        return msg;
    }

    public final int getAcknowledgeMode() {
        return this.acknowledgeMode;
    }

    @Nonnull
    public Connection getConnection() throws JMSException {
        Connection c = this.connection;
        if (c == null) this.connection = c = getConnectionFactory().createConnection();
        return c;
    }

    @Nonnull
    public ConnectionFactory getConnectionFactory() throws JMSException {
        final ConnectionFactory cf = this.connectionFactory;
        if (cf == null) throw new javax.jms.IllegalStateException("closed");
        return cf;
    }

    @Nonnull
    public MessageConsumer getConsumer() throws JMSException {
        if (this.consumer == null) this.consumer = createConsumer(null);
        return this.consumer;
    }

    @Nonnull
    public Destination getDestination() throws JMSException {
        Destination d = this.destination;
        if (d == null) {
            final JmsDestinationRef ref = this.destinationRef;
            if (ref == null) throw new javax.jms.IllegalStateException("closed");
            d = ref.createDestination(getSession());
            if (d == null) throw new JMSException("JMSDestinationRef.createDestination() returned null: " + ref);
            this.destination = d;
        }
        return d;
    }

    /**
   * @deprecated Replaced by {@link #getProducer()}.
   */
    @Deprecated
    public MessageProducer getMessageProducer() throws JMSException {
        return getProducer();
    }

    @Nonnull
    public MessageProducer getProducer() throws JMSException {
        MessageProducer p = this.producer;
        if (p == null) this.producer = p = getSession().createProducer(getDestination());
        return p;
    }

    @Nonnull
    public Session getSession() throws JMSException {
        Session s = this.session;
        if (s == null) this.session = s = getConnection().createSession(this.transactional, this.acknowledgeMode);
        return s;
    }

    public final boolean isOpen() {
        return this.connectionFactory != null;
    }

    public final boolean isTransacted() {
        return this.transactional;
    }

    @CheckForNull
    public Message poll() throws JMSException {
        return poll(null);
    }

    @CheckForNull
    public Message poll(@Nullable final String selector) throws JMSException {
        if ((selector == null) || selector.isEmpty()) return getConsumer().receiveNoWait();
        final MessageConsumer consumer = createConsumer(selector);
        try {
            return consumer.receiveNoWait();
        } finally {
            consumer.close();
        }
    }

    @CheckForNull
    public Message pollId(final String id) throws JMSException {
        return poll("JMSMessageID = '" + id + "'");
    }

    public void rollback() throws JMSException {
        getSession().rollback();
    }

    public void send(final Message msg) throws JMSException {
        send(msg, null);
    }

    public void send(final Message msg, @Nullable final MessageProducerParameters producerParameters) throws JMSException {
        CheckArg.notNull(msg, "msg");
        if (producerParameters == null) getProducer().send(msg); else getProducer().send(msg, producerParameters.deliveryMode, producerParameters.priority, producerParameters.timeToLive);
    }

    public void start() throws JMSException {
        getConnection().start();
    }

    public void stop() throws JMSException {
        getConnection().stop();
    }

    @CheckForNull
    public JMSException tryClose() {
        try {
            close();
            return null;
        } catch (final JMSException ex) {
            return ex;
        }
    }

    @CheckForNull
    public JMSException tryRollback() {
        try {
            rollback();
            return null;
        } catch (final JMSException ex) {
            return ex;
        }
    }
}
