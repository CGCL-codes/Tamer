package openrpg2.common.core.network;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author snowdog
 */
public class NetworkThreadPool {

    private List idle = new LinkedList();

    private List threadHandles = new LinkedList();

    private NetworkServer serverRef;

    private int nextId = 1;

    private Logger log = Logger.getLogger(this.getClass().getName());

    /** Creates a new instance of NetworkThreadPool */
    public NetworkThreadPool(NetworkServer serverReference, int initialSize) {
        serverRef = serverReference;
        for (int i = 0; i < initialSize; i++) {
            NetworkServiceThread nst = spawnServiceThread();
            synchronized (idle) {
                idle.add(nst);
            }
            synchronized (threadHandles) {
                threadHandles.add(nst);
            }
        }
    }

    protected void finalize() {
        shutdownThreads();
    }

    protected void shutdownThreads() {
        Iterator i = threadHandles.iterator();
        while (i.hasNext()) {
            NetworkServiceThread n = (NetworkServiceThread) i.next();
            log.finer("shutdownThreads(): terminating thread id: " + n.getName());
            n.sendStopSignal();
            i.remove();
        }
    }

    protected NetworkServiceThread getServiceThread() {
        NetworkServiceThread nst = null;
        synchronized (idle) {
            if (idle.size() > 0) {
                nst = (NetworkServiceThread) idle.remove(0);
            } else {
            }
        }
        return nst;
    }

    protected void returnServiceThread(NetworkServiceThread nst) {
        synchronized (idle) {
            idle.add(nst);
        }
    }

    private NetworkServiceThread spawnServiceThread() {
        NetworkServiceThread nst = new NetworkServiceThread(this, this.serverRef, nextId);
        nst.setName("NetworkThread-" + nextId);
        log.finer("spawnServiceThread() creating new thread. (" + nst.getName() + ")");
        nextId++;
        nst.start();
        return nst;
    }
}
