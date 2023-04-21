package org.ikasan.tools.messaging.destination;

import java.util.HashMap;
import java.util.Map;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import org.ikasan.tools.messaging.model.MapMessageWrapper;
import org.ikasan.tools.messaging.model.MessageWrapper;
import org.ikasan.tools.messaging.model.TextMessageWrapper;
import org.ikasan.tools.messaging.subscriber.BaseSubscriber;
import org.ikasan.tools.messaging.subscriber.MessageWrapperListenerSubscriber;
import org.ikasan.tools.messaging.subscriber.listener.MessageWrapperListener;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class DestinationHandle implements Comparable<DestinationHandle> {

    private String destinationPath;

    private Destination destination;

    private ConnectionFactory connectionFactory;

    private Map<String, BaseSubscriber> subscriptions = new HashMap<String, BaseSubscriber>();

    public Map<String, BaseSubscriber> getSubscriptions() {
        return subscriptions;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public DestinationHandle(String destinationPath, Destination destination, ConnectionFactory connectionFactory) {
        super();
        this.destinationPath = destinationPath;
        this.destination = destination;
        this.connectionFactory = connectionFactory;
    }

    public void publishMessage(final MessageWrapper messageWrapper, int priority) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPriority(priority);
        jmsTemplate.setExplicitQosEnabled(true);
        try {
            jmsTemplate.send(destination, new MessageCreator() {

                public Message createMessage(Session session) throws JMSException {
                    Message message = null;
                    if (messageWrapper instanceof TextMessageWrapper) {
                        message = session.createTextMessage(((TextMessageWrapper) messageWrapper).getText());
                    } else if (messageWrapper instanceof MapMessageWrapper) {
                        message = session.createMapMessage();
                        Map<String, Object> map = ((MapMessageWrapper) messageWrapper).getMap();
                        for (String mapKey : map.keySet()) {
                            Object mapValue = map.get(mapKey);
                            ((MapMessage) message).setObject(mapKey, mapValue);
                        }
                    }
                    return message;
                }
            });
        } catch (JmsException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSubscription(String subscriptionName, MessageWrapperListener messageListener) {
        if (subscriptions.get(subscriptionName) != null) {
            throw new IllegalStateException("Subscriber already exists for [" + destinationPath + "]");
        }
        MessageWrapperListenerSubscriber subscription = new MessageWrapperListenerSubscriber(connectionFactory, destination, messageListener);
        subscriptions.put(subscriptionName, subscription);
    }

    public void destroySubscription(String subscriptionName) {
        BaseSubscriber subscriber = subscriptions.get(subscriptionName);
        if (subscriber != null) {
            subscriber.shutdown();
        }
        subscriptions.remove(subscriptionName);
    }

    public int compareTo(DestinationHandle other) {
        return this.getDestinationPath().compareTo(other.getDestinationPath());
    }
}
