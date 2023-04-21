package org.exolab.jms.tools.migration.proxy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import org.exolab.jms.persistence.PersistenceException;
import org.exolab.jms.persistence.SQLHelper;
import org.exolab.jms.tools.migration.Store;
import org.exolab.jms.tools.migration.StoreIterator;

/**
 * Provides persistency for {@link Message} instances.
 *
 * @author <a href="mailto:tma#netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 07:07:12 $
 */
public class MessageStore implements Store, DBConstants {

    /**
     * The destination store.
     */
    private final DestinationStore _destinations;

    /**
     * The database connection.
     */
    private final Connection _connection;

    /**
     * Construct a new <code>MessageStore</code>.
     *
     * @param destinations the destination store
     * @param connection the database connection.
     */
    public MessageStore(DestinationStore destinations, Connection connection) {
        _destinations = destinations;
        _connection = connection;
    }

    /**
     * Export the collection.
     *
     * @return an iterator over the collection
     * @throws JMSException         for any JMS error
     * @throws PersistenceException for any persistence error
     */
    public StoreIterator exportCollection() throws JMSException, PersistenceException {
        List messageIds = getMessageIds();
        return new MessageIterator(messageIds);
    }

    /**
     * Import a collection.
     *
     * @param iterator an iterator over the collection
     * @throws JMSException         for any JMS error
     * @throws PersistenceException for any persistence error
     */
    public void importCollection(StoreIterator iterator) throws JMSException, PersistenceException {
        while (iterator.hasNext()) {
            Message message = (Message) iterator.next();
            add(message);
        }
    }

    /**
     * Returns the number of elements in the collection.
     *
     * @return the number of elements in the collection
     * @throws PersistenceException for any persistence error
     */
    public int size() throws PersistenceException {
        return getMessageIds().size();
    }

    /**
     * Add a message.
     *
     * @param message the message to add
     * @throws JMSException         for any JMS error
     * @throws PersistenceException for any persistence error
     */
    public synchronized void add(Message message) throws JMSException, PersistenceException {
        MessageHandler handler = MessageHandlerFactory.create(message, _destinations, _connection);
        handler.add(message);
    }

    /**
     * Returns a message for a given identifier.
     *
     * @param messageId the identity of the message
     * @return the message corresponding to <code>messageId</code> or
     *         <code>null</code> if no such message exists
     * @throws PersistenceException for any persistence error
     */
    public Message get(String messageId) throws JMSException, PersistenceException {
        Message result = null;
        PreparedStatement select = null;
        ResultSet set = null;
        try {
            select = _connection.prepareStatement("select message_type from " + MESSAGE_TABLE + " where message_id = ?");
            select.setString(1, messageId);
            set = select.executeQuery();
            if (set.next()) {
                String type = set.getString("message_type");
                String qualifiedType = "javax.jms." + type;
                MessageHandler handler = MessageHandlerFactory.create(qualifiedType, _destinations, _connection);
                result = handler.get(messageId);
            }
        } catch (SQLException exception) {
            throw new PersistenceException("Failed to get message with JMSMessageID=" + messageId, exception);
        } finally {
            SQLHelper.close(set);
            SQLHelper.close(select);
        }
        return result;
    }

    /**
     * Returns all message identifiers.
     *
     * @return a list of message identifiers
     * @throws PersistenceException for any persistence error
     */
    public List getMessageIds() throws PersistenceException {
        ArrayList result = new ArrayList();
        PreparedStatement select = null;
        ResultSet set = null;
        try {
            select = _connection.prepareStatement("select message_id from " + MESSAGE_TABLE);
            set = select.executeQuery();
            while (set.next()) {
                String messageId = set.getString("message_id");
                result.add(messageId);
            }
        } catch (SQLException exception) {
            throw new PersistenceException("Failed to get message ids", exception);
        } finally {
            SQLHelper.close(set);
            SQLHelper.close(select);
        }
        return result;
    }

    private class MessageIterator implements StoreIterator {

        private Iterator _iterator;

        public MessageIterator(List messageIds) {
            _iterator = messageIds.iterator();
        }

        public boolean hasNext() {
            return _iterator.hasNext();
        }

        public Object next() throws JMSException, PersistenceException {
            String messageId = (String) _iterator.next();
            return get(messageId);
        }
    }
}
