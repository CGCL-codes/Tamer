package org.tanso.fountain.router.http.gateway;

import java.util.HashMap;
import org.tanso.fountain.interfaces.func.routing.IGateway;
import org.tanso.fountain.interfaces.func.routing.IQueue;

/**
 * The class implements IGateway interface and it implements a simple gateway system.
 * @author Song Huanhuan
 *
 */
public class RequestGateway implements IGateway {

    /**
	 * queueGageway:The signal instance of the class.
	 * map:The internal data constructor of the gateway system.
	 * queueCount:The current count of the queue in the gateway system.
	 */
    private static IGateway queueGateway = new RequestGateway();

    private HashMap<String, IQueue> map;

    private int queueCount = 0;

    /**
	 * Get the current count of queue in the gateway system.
	 * @return
	 * 		The count of queues.
	 */
    public synchronized int getQueueCount() {
        return queueCount;
    }

    /**
	 * Get map of the class.
	 * @return
	 * 		
	 */
    public synchronized HashMap<String, IQueue> getMap() {
        return map;
    }

    /**
	 * The private constructor of the class.
	 *
	 */
    private RequestGateway() {
        map = new HashMap<String, IQueue>();
    }

    /**
	 * Get the signal instance of the class.
	 * @return
	 * 		The singal instance of the class.
	 */
    public static synchronized IGateway getGateway() {
        return queueGateway;
    }

    /**
	 * Add queue to the gateway system.
	 * @param
	 * 		QID:The id of the queue to be added to the gateway system.
	 * 		queue:The queue to be added to the gateway system.
	 */
    public synchronized void addQ(String QID, IQueue queue) {
        map.put(QID, queue);
        queueCount++;
        this.notifyAll();
    }

    /**
	 * Get queue with specific id QID.
	 * @param
	 * 		QID:The id of the queue to be gotten.
	 * @return	
	 * 		The specific queue with id QID.
	 */
    public synchronized IQueue getQ(String QID) {
        return map.get(QID);
    }

    /**
	 * Remove specific queue with id QID.
	 * @param
	 * 		QID:The id of the queue to be removed.
	 */
    public synchronized void removeQ(String QID) {
        try {
            while (queueCount <= 0) this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        map.remove(QID);
        queueCount--;
    }
}
