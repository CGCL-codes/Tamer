package de.iritgo.aktera.startup;

/**
 * Interface for classes that can be started during the server's boot process.
 */
public interface StartupHandler {

    /**
	 * This method is called when the component should start up.
	 */
    public abstract void startup() throws StartupException;

    /**
	 * This method is called when the component should shut down.
	 */
    public abstract void shutdown() throws ShutdownException;
}
