package org.exolab.jms.client;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.JMSException;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.naming.Reference;
import org.exolab.jms.common.uuid.UUID;

/**
 * A temporary queue is created by a client through a session and has
 * a lifetime of the session's connection.
 *
 * @version     $Revision: 1.2 $ $Date: 2005/12/20 20:36:43 $
 * @author      <a href="mailto:jima@exoffice.com">Jim Alateras</a>
 */
public class JmsTemporaryQueue extends JmsQueue implements TemporaryQueue, JmsTemporaryDestination {

    /**
     * Used for serialization
     */
    static final long serialVersionUID = 2;

    /**
     * The identifier of the connection that created this temporary
     * destination.
     */
    private long _connectionId;

    /**
     * Reference to the connection that owns this deztination.
     */
    private transient JmsConnection _connection = null;

    /**
     * Constructor provided for serialization.
     */
    public JmsTemporaryQueue() {
    }

    /**
     * Construct a new <code>JmsTemporaryTopic</code>.
     *
     * @param connection the owning connection
     */
    private JmsTemporaryQueue(JmsConnection connection) {
        super(TEMP_QUEUE_PREFIX + UUID.next());
        _connection = connection;
        _connectionId = connection.getConnectionId();
    }

    public void delete() throws JMSException {
        _connection.deleteTemporaryDestination(this);
    }

    public JmsConnection getOwningConnection() {
        return _connection;
    }

    public long getConnectionId() {
        return _connectionId;
    }

    public Reference getReference() {
        return null;
    }

    public boolean validForConnection(JmsConnection connection) {
        boolean result = false;
        if (connection != null && connection.getConnectionId() == _connectionId) {
            result = true;
        }
        return result;
    }

    public void writeExternal(ObjectOutput stream) throws IOException {
        stream.writeLong(serialVersionUID);
        stream.writeLong(_connectionId);
        super.writeExternal(stream);
    }

    public void readExternal(ObjectInput stream) throws IOException, ClassNotFoundException {
        long version = stream.readLong();
        if (version == serialVersionUID) {
            _connectionId = stream.readLong();
            super.readExternal(stream);
        } else {
            throw new IOException("JmsTemporaryQueue with version " + version + " is not supported.");
        }
    }

    /**
     * Construct a new temporary queue.
     *
     * @param connection the connection owns that the queue
     * @return a new temporary queue
     */
    public static TemporaryQueue create(JmsConnection connection) {
        return new JmsTemporaryQueue(connection);
    }
}
