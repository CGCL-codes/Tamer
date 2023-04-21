package com.threerings.riposte.server;

/**
 * Riposte Service dispatchers must implement this method.  Given the method id and args array,
 * they are responsible for calling the correct service method on the Service implementor.<br/><br/>
 *
 * The implementors of these interfaces are not usually written by hand - they are usually generated
 * by the genriposte ant task, defined in {@link com.threerings.riposte.tools.GenRiposteTask}.  If
 * this method is used, the service interface is required to have a service id defined in a public
 * static string:<br/><br/>
 *
 * {@code public static final int SERVICE_ID = 1;}
 */
public interface PostDispatcher {

    /**
     * Called by the RiposteManager when a service call for this service arrives at the server.
     *
     * @param methodId The id of the method being called.  The dispatcher is responsible for
     *                 maintaining a mapping of method id to service method.
     * @param args The args for the method being called.
     *
     * @return If this method returns a value, return it here.  If not, return null.
     *
     * @throws Exception If the service method throws an exception, let it pass through here.
     *                   {@link PostManager} will catch the exception and send a
     *                   {@link com.threerings.riposte.data.StreamableError} to the client with the
     *                   exception's message.
     */
    public Object dispatchRequest(int methodId, Object[] args) throws Exception;

    /**
     * Called by RiposteManager to increase the accuracy of error logs in a generic
     * catch (Exception) block
     */
    public String getMethodName(int methodId);
}
