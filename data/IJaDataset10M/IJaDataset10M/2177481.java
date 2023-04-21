package org.exolab.jms.message;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.JMSException;

/**
 * This class implements the JMSCorrelationID message header property
 *
 * @version     $Revision: 1.1 $ $Date: 2004/11/26 01:50:43 $
 * @author      <a href="mailto:mourikis@exolab.org">Jim Mourikis</a>
 * @see         javax.jms.Message
 */
class CorrelationId implements Externalizable {

    /**
     * Object version no. for serialization
     */
    static final long serialVersionUID = 1;

    /**
     * Possible usages
     */
    static final int APPLICATION_USE = 1;

    static final int PROVIDER_USE = 2;

    static final int PROVIDER_NATIVE = 3;

    /**
     * What is Correlation Id is used for
     */
    private int _usage = 0;

    /**
     * Link to another message
     */
    private MessageId _id = null;

    /**
     * Application specific link
     */
    private String _clientId = null;

    /**
     * Default constructor for externalization support
     */
    public CorrelationId() {
    }

    public CorrelationId(String id) throws JMSException {
        if (id.startsWith(MessageId.PREFIX)) {
            _usage = PROVIDER_USE;
            _id = new MessageId(id);
        } else {
            _usage = APPLICATION_USE;
            _clientId = id;
        }
    }

    public CorrelationId(byte[] id) throws JMSException {
        throw new UnsupportedOperationException("Provider native correlation identifier not supported");
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(serialVersionUID);
        out.writeInt(_usage);
        if (_usage == APPLICATION_USE) {
            out.writeInt(_clientId.length());
            out.writeChars(_clientId);
        } else if (_usage == PROVIDER_USE) {
            _id.writeExternal(out);
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        long version = in.readLong();
        if (version == serialVersionUID) {
            _usage = in.readInt();
            if (_usage == APPLICATION_USE) {
                int len = in.readInt();
                int i;
                StringBuffer buf = new StringBuffer(len);
                for (i = 0; i < len; i++) {
                    buf.append(in.readChar());
                }
                _clientId = buf.toString();
            } else if (_usage == PROVIDER_USE) {
                _id = new MessageId();
                _id.readExternal(in);
            }
        } else {
            throw new IOException("Incorrect version enountered: " + version + ". This version = " + serialVersionUID);
        }
    }

    public String getString() throws JMSException {
        String result = null;
        if (_usage == APPLICATION_USE) {
            result = _clientId;
        } else if (_usage == PROVIDER_USE) {
            result = _id.toString();
        } else {
            throw new JMSException("Unknown correlation");
        }
        return result;
    }

    public byte[] getBytes() throws JMSException {
        throw new UnsupportedOperationException("Provider native correlation identifier not supported");
    }
}
