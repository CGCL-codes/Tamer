package jifx.connection.multiplexer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TopicSubscriber;
import org.apache.log4j.Logger;

/**
 * This class receives messages from a queue and pass it 
 * to a the multiplexer in order to process it. 
 */
public class ReceiveThread implements Runnable {

    static Logger logger = Logger.getLogger(Multiplexer.class);

    private boolean activate;

    private Multiplexer multiplexer;

    private TopicSubscriber topicSuscriber;

    public ReceiveThread(TopicSubscriber topicSuscriber, Multiplexer mux) {
        activate = true;
        this.topicSuscriber = topicSuscriber;
        multiplexer = mux;
    }

    /**
	 * Receives messages from the queue and invokes multiplexer in order to procees the message. 
	 */
    public void run() {
        while (activate) {
            try {
                Message msg = topicSuscriber.receive(1000);
                if (msg != null) {
                    multiplexer.onMessage(msg);
                }
            } catch (JMSException e) {
                logger.error(multiplexer.getChannelName() + "| " + e.getMessage() + "|");
            }
        }
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }
}
