package org.eaiframework.impl.activemq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eaiframework.Message;
import org.eaiframework.MessageException;
import org.eaiframework.MessageProducer;

public class ActiveMQMessageProducer implements MessageProducer {

    private static Log log = LogFactory.getLog(ActiveMQMessageProducer.class);

    public void produceMessage(Message message) throws MessageException {
        String destinationName = message.getDestination();
        log.debug("sending message: " + message + " to destination: " + destinationName);
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(destinationName);
            javax.jms.MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            ObjectMessage oMessage = session.createObjectMessage(message);
            producer.send(oMessage);
            session.close();
            connection.close();
        } catch (Exception e) {
            throw new MessageException(e);
        }
    }
}
