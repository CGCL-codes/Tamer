package org.vmaster.client;

/**
 *  This is the interface that the VMasterGUI and VMasterCommandLine
 *  must follow.
 */
public interface VMasterClient {

    /**
	 * Method to start the client whether it be the command line
	 * or the GUI passing in the parameters to either one.
	 */
    public void start(String[] args);
}
