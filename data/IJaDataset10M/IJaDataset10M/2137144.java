package no.ugland.utransprod.gui;

/**
 * Interface som brukes for � sjekke om et vindu kan lukkes
 * 
 * @author abr99
 * 
 */
public interface Closeable {

    /**
	 * Sjekker om vindu kan lukkes
	 * 
	 * @param actionString
	 * @param window
	 * @return true dersom vindu kan lukkes
	 */
    boolean canClose(String actionString, WindowInterface window);
}
