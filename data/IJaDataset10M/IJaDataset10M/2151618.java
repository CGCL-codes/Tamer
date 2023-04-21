package com.hyper9.vmm.client;

/**
 * An interface for classes that send a setNetwork operation.
 * 
 * @author akutz
 * 
 */
public interface SendsSetNetworkOps {

    /**
     * Enables the network.
     * 
     * @param authCookie The authentication cookie.
     * @param networkID The ID of the network to configure.
     * @param receiver The receiver of the setNetwork op.
     */
    void setNetworkEnabled(String authCookie, String networkID, ReceivesSetNetworkOps receiver);

    /**
     * Disables the network.
     * 
     * @param authCookie The authentication cookie.
     * @param networkID The ID of the network to configure.
     * @param receiver The receiver of the setNetwork op.
     */
    void setNetworkDisabled(String authCookie, String networkID, ReceivesSetNetworkOps receiver);
}
