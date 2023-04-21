package se.marcus.guice.jms;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * GuiceJmsTemplate
 * @author krummas@gmail.com
 */
public interface GuiceJmsTemplate {

    public void send(MessageCreator messageCreator) throws JMSException;

    public Message receive() throws JMSException;

    public void close() throws JMSException;
}
