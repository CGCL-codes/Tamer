package org.aphis.core;

/**
 * AphisManager is the base class for all Aphis services, allowing association of an 
 * {@link org.aphis.core.AphisOriginator} with the service for configuration purposes 
 * 
 * @author Greg Soertsz
 * @version 1.0
 * @see {@link org.aphis.core.AphisOriginator}
 */
public class AphisManager {

    /**
	 * The owner component
	 */
    protected AphisOriginator owner = null;

    /**
	 * Constructor for AphisManager that requires an AphisOriginator
	 * 
	 * @param comp the owner of this service
	 */
    public AphisManager(AphisOriginator comp) {
        this.owner = comp;
    }
}
