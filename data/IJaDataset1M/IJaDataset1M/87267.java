package topchat.server.interfaces;

/**
 * Interface implemented by the mediator that connects the configuration module
 * with the other components of the server.
 */
public interface ConfigurationMediator {

    /**
	 * Method called to set the configuration module connected to the mediator.
	 * 
	 * @param confHandler
	 *            the configuration module
	 */
    public void setConfigurationHandler(ConfigurationHandlerInterface confHandler);
}
