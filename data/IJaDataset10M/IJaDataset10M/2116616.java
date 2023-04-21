package org.bs.mdi.core;

/**
 * Represents the information contained in a document.
 */
public abstract class RootData extends Data {

    /**
	 * Gets the Printer interface for this document.
	 * If this method returns a working implementation of the <code>Printer</code>
	 * interface, printing functionality is enabled and the user may perform
	 * operations such as showing a print preview or starting a print job. 
	 * @return	the printer interface, or null if this document cannot be printed
	 */
    public abstract Printer getPrinter();
}
