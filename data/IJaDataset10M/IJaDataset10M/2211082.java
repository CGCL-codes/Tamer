package server;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Title:        ClusterServer
 * Description:  A clustered server to replicate state across separate JVMs.
 * Copyright:    Copyright (c) 2001
 * Company:
 *
 * Basic implementation of the Publisher interface. Manages a set of
 * Subscribers and notifies them of events.
 *
 * @author Miron Roth
 * @version 1.0
 */
public class BasePublisher implements Publisher {

    /** A set containing all subscribers */
    private Set subscribers;

    /** Default constructor */
    public BasePublisher() {
        subscribers = new HashSet();
    }

    /**
	 *  Subscribe to the publisher to be notified of incoming data
	 *  @param client The subscriber
	 */
    public void subscribe(Subscriber client) {
        subscribers.add(client);
    }

    /**
	 *  Unsubscribe from the publisher
	 *  @param client The subscriber
	 */
    public void unsubscribe(Subscriber client) {
        subscribers.remove(client);
    }

    /**
	 *  Notifies all subscribers of this Publishers
	 *  @param data The data to notify subscribers with
	 */
    public void publish(Object data) {
        Iterator i = subscribers.iterator();
        while (i.hasNext()) ((Subscriber) i).notify(data);
    }
}
