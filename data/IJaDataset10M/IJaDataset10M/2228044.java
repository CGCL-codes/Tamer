package org.harpiamonitor.core.monitor;

/**
 * This interface must be implemented for all monitors in the system. It defines
 * methods whose purpose is perform polling (health check).
 * 
 * @author Andr� Thiago
 * 
 */
public interface Poller {

    /**
	 * Poll a resource for checking if it is answering in the expected way. If
	 * not, an event must be generated.
	 * @return the status of pooling
	 */
    public PollStatus poll();
}
