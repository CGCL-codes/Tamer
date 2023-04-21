package org.nightlabs.print;

/**
 * Used to create (unconfigured) {@link PrinterInterface}s.
 * The {@link PrinterInterfaceManager} uses this type of factory
 * to be able to create different types of interfaces.
 * 
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 *
 */
public interface PrinterInterfaceFactory {

    /**
	 * Create a new and unconfigured {@link PrinterInterface}.
	 * 
	 * @return A new and unconfigured {@link PrinterInterface}.
	 */
    public PrinterInterface createPrinterInterface();
}
