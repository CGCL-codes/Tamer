package fr.xebia.sample.springframework.jms.requestreply;

import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.JMSException;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:cyrille.leclerc@pobox.com">Cyrille Le Clerc</a>
 */
public class RequestReplyServer {

    private static final Logger logger = Logger.getLogger(RequestReplyServer.class);

    protected AtomicInteger invocationsCounter = new AtomicInteger();

    public String sayHello(String message) throws JMSException {
        int counterValue = this.invocationsCounter.incrementAndGet();
        String result = "Hello " + message + ". Request# " + counterValue;
        logger.debug(result);
        return result;
    }

    public int getInvocationsCounter() {
        return this.invocationsCounter.get();
    }
}
