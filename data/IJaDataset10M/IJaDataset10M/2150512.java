package com.teletalk.jserver.net.sns.server;

import java.util.List;
import com.teletalk.jserver.net.sns.Service;

/**
 * The inteface of an SNS server which is available for remote (RPC) access. 
 * 
 * @author Tobias L�fstrand
 * 
 * @since 2.1 (20050502)
 */
public interface SnsServer {

    /** 
    * The version of the interface.<br>
    * <br>
    * <b>Version 2</b>:<br>
    * * Routing of administration messages expanded to allow full access to components in a remote server.<br> 
    * <br>
    */
    public static final int INTERFACE_VERSION = 2;

    /**
    * Gets the version of this interface.
    *  
    * @since 2.1.3 (20060324)
    */
    public int getInterfaceVersion();

    /**
    * Gets a list of all servers connected to the SNS server.
    */
    public List getServers();

    /**
    * Gets a list of all servers connected to the SNS server, containing the service with the specified name.
    */
    public List getServers(String serviceName);

    /**
    * Gets a list of all available services in the SNS server.
    */
    public List getServices();

    /**
    * Gets the service with the specified name from the SNS server.
    */
    public Service getService(String serviceName);
}
