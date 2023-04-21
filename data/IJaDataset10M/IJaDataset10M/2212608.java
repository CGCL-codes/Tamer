package org.omegahat.Simulation.MCMC;

import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import java.util.*;

/** 
 *  A simple class providing methods implementing the interface NotifyingObject
 */
public abstract class NotifyingMCMCObject implements NotifyingObject {

    protected Hashtable listeners = new Hashtable();

    public class MyHandle implements MCMCListenerHandle {

        public MyHandle() {
        }

        ;
    }

    public MCMCListenerHandle registerListener(MCMCListener listener) {
        MCMCListenerHandle handle = new MyHandle();
        listeners.put(handle, listener);
        return handle;
    }

    public void unregisterListener(MCMCListenerHandle handle) {
        listeners.remove(handle);
    }

    protected void notifyAll(MCMCEvent e) {
        MCMCListener l = null;
        Enumeration iterator = listeners.elements();
        while (iterator.hasMoreElements()) {
            l = (MCMCListener) iterator.nextElement();
            if (l != null) l.notify(e);
        }
    }

    /** Generate the next state from the current one */
    protected abstract MCMCState generate(MCMCState current);
}
