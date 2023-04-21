package com.cliftonsnyder.clutch.mr.task;

import java.io.IOException;
import java.util.Hashtable;
import com.cliftonsnyder.clutch.util.ClutchState;

/**
 * An abstraction that handles the "messy" business of migrating MapTasks
 * 
 * @author Clifton L. Snyder
 * @created September 27, 2006
 * 
 */
public abstract class AbstractMapTaskFactory implements MapTaskFactory {

    protected ClutchState state;

    private Hashtable<String, Integer> requests;

    public synchronized MapTask getMapTask(String requestId) throws IOException {
        Integer count = requests.remove(requestId);
        requests.put(requestId, (count == null) ? 1 : count + 1);
        return createMapTask();
    }

    public synchronized MapTask getMapTask(String requestId, long offset, long length) throws IOException {
        Integer count = requests.remove(requestId);
        requests.put(requestId, (count == null) ? 1 : count + 1);
        return createMapTask(offset, length);
    }

    public void setState(ClutchState state) {
        this.state = state;
    }

    public void putMapTask(MapTask task) {
    }

    public void init() {
        requests = new Hashtable<String, Integer>();
    }

    public String toString() {
        return requests.toString();
    }
}
