package jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import exception.JMSServiceException;

public abstract class JMSListenerService extends JMSService {

    private MessageConsumer receiver;

    public JMSListenerService(String jmsJNDIName, String destinationName) throws JMSServiceException {
        super(jmsJNDIName, destinationName);
    }

    @Override
    protected void prepare(Session session, Destination dest) throws JMSException {
        receiver = session.createConsumer(dest);
        receiver.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                onIncomingMessage(message);
            }
        });
    }

    public abstract void onIncomingMessage(Message message);
}
