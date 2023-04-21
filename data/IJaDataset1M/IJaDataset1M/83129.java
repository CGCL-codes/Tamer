package org.enjavers.jethro.api;

/**
 * Implementors of this interface are intended to be managed by a 
 * single instance of JethroManager; i.e., DTObject, DTOMask, DynamicDAO,
 * Metadata and generally each object accessible via a JethroProvider that
 * also needs to refer itself to its Manager. 
 * 
 * @author Alessandro Lombardi, Fabrizio Gambelunghe
 * @since 1.2
 */
public interface Manageable {

    /**
	 * @return the instance of JethroManager that 'owns' this Manageable
	 * object.
	 */
    public JethroManager getEnclosingManager();
}
