package com.google.gsa;

/**
 * WebProcess instance used as a HTTP connection in the Authentication and 
 * Authorization modules
 * 
 */
public class WebProcessor extends IWebProcess {

    /**
     * Class constructor - default
     * 
     */
    public WebProcessor() {
        super();
    }

    /**
     * Class contructor
     * <p>
     * Support for connection management
     * 
     * @param maxConnectionsPerHost maximum number of HTTP connex per host
     * @param maxTotalConnections maximum total number of HTTP connex
     */
    public WebProcessor(int maxConnectionsPerHost, int maxTotalConnections) {
        super(maxConnectionsPerHost, maxTotalConnections);
    }
}
