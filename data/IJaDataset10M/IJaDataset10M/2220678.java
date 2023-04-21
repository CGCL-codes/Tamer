package org.exolab.jms.tools.migration.proxy;

import java.sql.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import org.exolab.jms.message.TextMessageImpl;
import org.exolab.jms.persistence.PersistenceException;

/**
 * Handler for messages of type <code>javax.jms.TextMessage</code>.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 07:07:12 $
 */
class TextMessageHandler extends AbstractMessageHandler {

    /**
     * Construct a new <code>TextMessageHandler</code>.
     *
     * @param destinations the destination store
     * @param connection   the database connection
     */
    public TextMessageHandler(DestinationStore destinations, Connection connection) {
        super(destinations, connection);
    }

    /**
     * Returns the type of message that this handler supports.
     *
     * @return the type of message
     */
    protected String getType() {
        return "TextMessage";
    }

    /**
     * Create a new message.
     *
     * @return a new message
     * @throws JMSException for any JMS error
     */
    protected Message newMessage() throws JMSException {
        return new TextMessageImpl();
    }

    /**
     * Returns the body of the message.
     *
     * @param message the message
     * @return the body of the message
     * @throws JMSException for any JMS error
     */
    protected Object getBody(Message message) throws JMSException {
        return ((TextMessageImpl) message).getText();
    }

    /**
     * Populate the message body.
     *
     * @param body    the message body
     * @param message the message to populate
     * @throws JMSException         for any JMS error
     * @throws PersistenceException for any persistence error
     */
    protected void setBody(Object body, Message message) throws JMSException, PersistenceException {
        if (body != null && !(body instanceof String)) {
            throw new JMSException("Expected String body for TextMessage with JMSMessageID=" + message.getJMSMessageID() + " but got type " + body.getClass().getName());
        }
        TextMessageImpl text = (TextMessageImpl) message;
        text.setText((String) body);
    }
}
