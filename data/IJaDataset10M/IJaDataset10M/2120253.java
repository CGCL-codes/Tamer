package com.monoserv.interfaces;

import java.net.Socket;
import com.monoserv.InstanceModifier;

/**
 * Module interface.  Simple methods to describe the life of a request.
 *
 * More on this later.
 *
 * @author Will Chapman
 */
public interface IModule {

    /**
     * Called to prep any information if needed
     *
     * @param config A copy of the configuration to help with any prep work.
     * @throws Exception If an error occurs
     */
    void start(IConfiguration config) throws Exception;

    /**
     * Called after start... only called once.
     * The module is responsible for creating its
     * own threads and ending them for communication.
     *
     * @param data The data to pass to the module
     * @return IModuleData The data to be passed to the next module (or back to the client).
     * @throws Exception If an error occurs.
     */
    IModuleData run(IModuleData data) throws Exception;

    /**
     * This is called at the end right before the connection
     * is closed.
     *
     * @throws Exception If an error occurs.
     */
    void end() throws Exception;

    /**
     * This is called with a copy of the accepted socket for the connection
     *
     * @param sock The accepted socket from the server to be used for communication.
     */
    public void SaveSocket(Socket sock);

    /**
     * Returns an InstanceModifier object for determining if we need to take any
     * kind of automated action after everything is said and done.
     *
     * @return InstanceModifier An InstanceModifier object.
     */
    public InstanceModifier getInstanceModifier();
}
