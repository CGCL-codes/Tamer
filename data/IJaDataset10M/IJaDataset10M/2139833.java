package org.sltech.punchclock.server.ejb.services;

import java.util.Map;
import javax.ejb.Remote;
import org.sltech.punchclock.server.ejb.PunchClockInterf;
import org.sltech.punchclock.server.exception.SLQueueException;
import org.sltech.punchclock.server.exception.SLServerException;

@Remote
public interface MessageBeanRemote extends PunchClockInterf {

    public static final String COMP_NAME = COMP_NAME_ROOT + "MessageBean/remote";

    public static final String JNDI_NAME = JNDI_NAME_ROOT + "MessageBean/remote";

    public void sendMessageToTopic(String topicName, String message) throws SLQueueException;

    public void sendMessageToQueue(boolean transacted, int acknowledgeMode, String queueName, String message, int deliveryMode, int priority, int timeToLive) throws SLQueueException;

    ;

    public void sendSelectiveMessageToQueue(boolean transacted, int acknowledgeMode, String queueName, Map<String, String> messageProperties, String message, int deliveryMode, int priority, int timeToLive) throws SLQueueException;

    public String recieveSelectiveMessageToQueue(boolean transacted, int acknowledgeMode, String queueName, String messageSelector, int timeOut) throws SLServerException;
}
