package org.dago.atacom.controller;

import org.dago.atacom.grammar.Command;

/**
 * Driver interface.
 */
public interface Driver {

    /**
	 * Gives the driver name
	 * @return the driver name 
	 */
    String getName();

    /**
	 * Connect the driver
	 * @throws DagoException when the connection to the driver failed
	 */
    void connect() throws org.dago.common.DagoException;

    /**
	 * Disconnect the driver
	 */
    void disconnect();

    /**
	 * Test the driver connection
	 * @return true if the driver is connected, false if the driver is not connected
	 */
    boolean isConnected();

    /**
	 * Ask the driver to execute the specified command
	 * @param command the command to execute
	 * @return true if the command has been executed, false otherwise
	 */
    boolean execute(Command command);
}
