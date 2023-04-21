package org.aphis.event.framework.spi;

import org.aphis.core.event.*;

/**
 * Interface describing the ability to service an AphisEvent
 * 
 * @author Greg Soertsz
 */
public interface AphisEventServicer {

    /**
	 * Method to service an AphisEvent. 
	 * This role is typically performed by the consumer of AphisEvent as part of a listener implementation
	 * 
	 * @param e the {@link org.aphis.core.event.AphisEvent} that needs to be serviced
	 * @throws AphisEventException if the AphisEvent could not be serviced
	 */
    public void service(AphisEvent e) throws AphisEventException;
}
