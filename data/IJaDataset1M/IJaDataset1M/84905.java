package org.nakedobjects.object.repository;

import org.nakedobjects.object.NakedObjects;
import java.util.Hashtable;
import org.apache.log4j.Logger;

public class NakedObjectsByThread extends NakedObjectsServer {

    private static final Logger LOG = Logger.getLogger(NakedObjectsByThread.class);

    public static NakedObjects createInstance() {
        if (getInstance() == null) {
            return new NakedObjectsByThread();
        } else {
            return getInstance();
        }
    }

    private Hashtable threads = new Hashtable();

    private NakedObjectsByThread() {
    }

    protected NakedObjectsData getLocal() {
        Thread thread = Thread.currentThread();
        NakedObjectsData local;
        synchronized (threads) {
            local = (NakedObjectsData) threads.get(thread);
            if (local == null) {
                local = new NakedObjectsData();
                synchronized (threads) {
                    threads.put(thread, local);
                }
                LOG.info("  creating local " + local + "; now have " + threads.size() + " locals");
            }
        }
        return local;
    }

    public String getDebugTitle() {
        return "Naked Objects Repository " + Thread.currentThread().getName();
    }
}
