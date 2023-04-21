package net.walend.somnifugi;

import javax.jms.QueueReceiver;
import javax.jms.Queue;
import javax.jms.JMSException;

/**
A receiver end for a Queue. Get this from the Session.

@author @dwalend@
@author @pwang@ added support for client acknowledgement.
 */
public class SomniQueueReceiver extends SomniMessageConsumer implements QueueReceiver {

    private SomniQueue queue;

    private String name;

    protected SomniQueueReceiver(SomniQueue queue, String name, SomniExceptionListener exceptionListener, SomniSession session) {
        super(queue.getTakable(), exceptionListener, session);
        this.name = name;
        this.queue = queue;
    }

    /** Gets the <CODE>Queue</CODE> associated with this queue receiver.
 
@return this receiver's <CODE>Queue</CODE> 
 
@exception JMSException if the JMS provider fails to get the queue for
                        this queue receiver
                        due to some internal error.
      */
    public Queue getQueue() throws JMSException {
        return queue;
    }

    protected SomniDestination getDestionation() {
        return queue;
    }

    public String getName() {
        return name;
    }
}
