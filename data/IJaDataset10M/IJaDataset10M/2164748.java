package org.frameworkset.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

/**
 * <p>
 * Title: JMSTopicSubscriber.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2010-1-18 ����11:35:12
 * @author biaoping.yin
 * @version 1.0
 */
public class JMSTopicSubscriber implements TopicSubscriber {

    TopicSubscriber consumer;

    ReceiveDispatcher receivor;

    public JMSTopicSubscriber(TopicSubscriber consumer, ReceiveDispatcher receivor) {
        this.consumer = consumer;
        this.receivor = receivor;
    }

    public boolean getNoLocal() throws JMSException {
        return consumer.getNoLocal();
    }

    public Topic getTopic() throws JMSException {
        return consumer.getTopic();
    }

    public void close() throws JMSException {
        consumer.close();
    }

    public MessageListener getMessageListener() throws JMSException {
        return consumer.getMessageListener();
    }

    public String getMessageSelector() throws JMSException {
        return consumer.getMessageSelector();
    }

    public Message receive() throws JMSException {
        return consumer.receive();
    }

    public Message receive(long arg0) throws JMSException {
        return consumer.receive(arg0);
    }

    public Message receiveNoWait() throws JMSException {
        return consumer.receiveNoWait();
    }

    public void setMessageListener(MessageListener arg0) throws JMSException {
        if (arg0 instanceof JMSMessageListener) {
            JMSMessageListener l = (JMSMessageListener) arg0;
            l.setReceivor(this.receivor);
        }
        this.consumer.setMessageListener(arg0);
    }
}
