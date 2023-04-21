package org.dmd.dmc;

/**
 * The DmcLoggerIF defines an entity that can handle logging functionality to
 * report problems from deep within the Dark Matter Framework. Due to the fact
 * that the operational environment for Dark Matter is quite varied, there's
 * no guarantee as to what type of logging interface is available and this 
 * interface just lets you plugin whatever logging mechanism you want.
 *
 */
public interface DmcLoggerIF {

    /**
	 * This method is called by the lazy resolution mechanisms if the logDeadReferences
	 * option has been selected on DmcOmni (
	 * @param referrer
	 * @param viaAttribute
	 * @param referenceTo
	 */
    public void logDeadReference(DmcObject referrer, DmcAttribute<?> viaAttribute, DmcObjectName referenceTo);
}
