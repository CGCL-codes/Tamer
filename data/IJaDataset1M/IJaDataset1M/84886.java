package mobat.satin;

import ibis.satin.impl.Satin;
import ibis.satin.impl.spawnSync.InvocationRecord;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import mobat.satin.messages.IbisMessageServer;

/**
 *
 * @author S.K. Smit
 */
public class SATINClient extends Thread implements ISATINClient {

    Method localMethod = null;

    Method remoteMethod = null;

    public void start() {
        Method[] methods = ibis.satin.impl.Satin.class.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals("callSatinLocalFunction")) {
                m.setAccessible(true);
                localMethod = m;
            }
            if (m.getName().equals("callSatinRemoteFunction")) {
                m.setAccessible(true);
                remoteMethod = m;
            }
        }
        super.start();
    }

    public void run() {
        System.out.println("Connecting");
        IbisMessageServer.getInstance();
        System.out.println("Preparing for jobs");
        Satin satin = new Satin();
        while (!satin.exiting) {
            if (!SATINClientsManager.hasMultiThreadedTask) {
                SATINClientsManager.prepareForJob();
                SATINClientsManager.loadingJob = true;
                InvocationRecord r = satin.algorithm.clientIteration();
                if (r != null && satin.so.executeGuard(r)) {
                    callSatinFunction(satin, r);
                } else {
                    SATINClientsManager.loadingJob = false;
                    satin.handleDelayedMessages();
                }
            } else {
                synchronized (this) {
                    try {
                        System.out.println(Satin.getSatin().ident + " went to rest");
                        SATINClientsManager.rest(this);
                        wait();
                        System.out.println(Satin.getSatin().ident + " was regenerated");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SATINClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        try {
            satin.exit();
        } catch (Exception e) {
        }
        SATINClientsManager.exit();
    }

    public void callSatinFunction(Satin s, InvocationRecord r) {
        if (r.getParent() != null && r.getParent().aborted) {
            r.decrSpawnCounter();
            return;
        }
        InvocationRecord oldParent = s.parent;
        s.onStack.push(r);
        s.parent = r;
        s.handleDelayedMessages();
        try {
            if (r.getOwner().equals(s.ident)) {
                localMethod.invoke(s, r);
            } else {
                remoteMethod.invoke(s, r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        s.parent = oldParent;
        s.onStack.pop();
    }

    public void setDummy() {
    }
}
