package fr.xebia.sample.springframework.jms;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;

/**
 * JMS Message sender sample.
 * 
 * @author <a href="mailto:cyrille.leclerc@pobox.com">Cyrille Le Clerc</a>
 */
public class SampleSender {

    protected JmsTemplate jmsTemplate;

    public void simpleSend(String message) {
        this.jmsTemplate.convertAndSend(message);
    }

    @Required
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
