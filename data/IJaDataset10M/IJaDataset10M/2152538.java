package org.esprit.ocm.client.rpc.oca;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface VirtualNetworkOcaServiceAsync {

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.esprit.ocm.client.rpc.oca.VirtualNetworkOcaService
     */
    void listAll(java.lang.String secret, java.lang.String endpoint, int filter, AsyncCallback<java.util.List<org.esprit.ocm.model.oca.VnetOca>> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.esprit.ocm.client.rpc.oca.VirtualNetworkOcaService
     */
    void allocate(java.lang.String secret, java.lang.String endpoint, java.lang.String template, AsyncCallback<java.lang.String> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.esprit.ocm.client.rpc.oca.VirtualNetworkOcaService
     */
    void delete(java.lang.String secret, java.lang.String endpoint, int vnetId, AsyncCallback<java.lang.String> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.esprit.ocm.client.rpc.oca.VirtualNetworkOcaService
     */
    void info(java.lang.String secret, java.lang.String endpoint, int vnetId, AsyncCallback<org.esprit.ocm.model.oca.VnetOca> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.esprit.ocm.client.rpc.oca.VirtualNetworkOcaService
     */
    void publish(java.lang.String secret, java.lang.String endpoint, int vnetId, AsyncCallback<java.lang.String> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.esprit.ocm.client.rpc.oca.VirtualNetworkOcaService
     */
    void unpublish(java.lang.String secret, java.lang.String endpoint, int vnetId, AsyncCallback<java.lang.String> callback);

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util {

        private static VirtualNetworkOcaServiceAsync instance;

        public static final VirtualNetworkOcaServiceAsync getInstance() {
            if (instance == null) {
                instance = (VirtualNetworkOcaServiceAsync) GWT.create(VirtualNetworkOcaService.class);
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "VirtualNetworkOcaService.rpc");
            }
            return instance;
        }

        private Util() {
        }
    }
}
