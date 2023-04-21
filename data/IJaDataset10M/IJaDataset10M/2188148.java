package org.tanso.fountain.router.http.gateway;

import org.tanso.fountain.interfaces.func.routing.IQElement;
import org.tanso.fountain.interfaces.func.routing.IQueue;
import org.tanso.fountain.interfaces.func.routing.IThrottle;

/**
 * The class implements the IThrottle interface and is used to the throttle of the queue.
 * @author Song huanhuan
 *
 */
public class QueueThrottle implements IThrottle {

    /**
	 * @param 
	 * 		queue:The queue to be checked.
	 * @return
	 * 		true:If the queue can be dequeued.
	 * 		false:If the queue can't be dequeued.
	 */
    public boolean canDequeue(IQueue queue) {
        return true;
    }

    /**
	 * @param
	 * 		qElement:The IQElement instance to be enqueued.
	 * @return
	 * 		true:If the queue can be enqueued.
	 * 		false:If the queue can't be enqueued.
	 */
    public boolean canEnqueue(IQElement qElement) {
        return true;
    }
}
