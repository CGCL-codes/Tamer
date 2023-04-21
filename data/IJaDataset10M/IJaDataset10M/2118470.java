package net.sourceforge.esw.transport;

/**
 * This is the entry point to the Transport system. Clients wishing to
 * use transport must first startup the Transport system using methods in this
 * object.
 * <p>
 *
 */
public interface ITransportDelegate {

    /****************************************************************************
   * Starts up the Transport system.
   */
    public void startup() throws Exception;

    /****************************************************************************
   * Starts up the Transport system at the specified port.
   *
   * @param aPort the port on which to start the trasport.
   */
    public void startup(int aPort) throws Exception;

    /****************************************************************************
   * Shuts down the Transport system
   */
    public void shutdown() throws Exception;

    /****************************************************************************
   * Creates a Proxy object that can be sent to remote clients that allows
   * remote method calls to be made against the specified object.
   *
   * @param aObject the Object to create a Proxy from
   *
   * @throws Exception if an error occured during creation of the Proxy.
   */
    public Object getProxy(Object aObject) throws Exception;
}
