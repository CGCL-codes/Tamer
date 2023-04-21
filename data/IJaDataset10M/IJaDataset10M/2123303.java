package com.unsins.business.jms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import javax.jms.*;

public class SpringConsumer implements MessageListener {

    private static final Log LOG = LogFactory.getLog(SpringConsumer.class);

    private JmsTemplate template;

    private String myId = "foo";

    private Destination destination;

    public void onMessage(Message message) {
        try {
            message.acknowledge();
            LOG.info(((TextMessage) message).getText() + ":SpringConsumer");
            LOG.info(((TextMessage) message).getStringProperty("next") + ":SpringConsumer");
        } catch (JMSException e) {
            LOG.error("Failed to acknowledge: " + e, e);
        }
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public JmsTemplate getTemplate() {
        return template;
    }

    public void setTemplate(JmsTemplate template) {
        this.template = template;
    }
}
