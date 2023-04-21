package edu.kgi.biobridge.gum;

/**
 * The Broker is the top-level interface in the driver hierarchy. All drivers
 * <b>must</b> implement the Broker interface. The Broker interface provides
 * the Services set, access to the EventHandler collection, and basic session
 * management.
 * 
 * @author Cameron Wellock
 */
public interface Broker {

    /**
 * Connect the broker to its native envrionment. This method is probably one 
 * of the first that will be called by the bridgehead in its run() implementation.
 * @param url An optional URL or other connection string needed by the broker.
 * Presumably this URL would be provided as a command-line parameter, as the 
 * bridgehead will probably not be able to provide such information.
 * @throws EConnectionFailure
 */
    public void connect(String url) throws EConnectionFailure;

    /**
 * Connect the broker to its native environment, passing in any useful options.
 * @param url An optional URL or other connection string needed by the broker.
 * @param options An entirely separate options list, used for internal broker-
 * driver options. Drivers cannot expect to find an option, and brokers cannot
 * demand that their options be acknowledged.
 * @throws EConnectionFailure
 */
    public void connect(String url, String[] options) throws EConnectionFailure;

    /**
 * Disconnect the broker from its native environment. This should be done by
 * the bridgehead just prior to shutdown, to allow the driver to clean up any
 * resources inform its native environment of its termination if necessary.
 */
    public void disconnect();

    /**
 * Add an event handler to the event handler set.
 * @param handler The event handler to add, may be the bridgehead itself.
 */
    public void addEventHandler(EventHandler handler);

    /**
 * Remove an event handler from the event handler set.
 */
    public void removeEventHandler(EventHandler handler);

    /**
 * Get the service set provided by the driver.
* @throws EConnectionFailure
 */
    public Services getServices() throws EConnectionFailure;
}
