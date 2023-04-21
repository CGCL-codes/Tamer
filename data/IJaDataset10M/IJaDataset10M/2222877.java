package com.griddynamics.openspaces.convergence.examples.dademo.ui.client;

import java.util.Map;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface ConvergenceDemoServiceAsync {

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.griddynamics.openspaces.convergence.examples.dademo.ui.client.ConvergenceDemoService
     */
    void login(String pwd, AsyncCallback<StandStatus> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.griddynamics.openspaces.convergence.examples.dademo.ui.client.ConvergenceDemoService
     */
    void logoff(AsyncCallback<StandStatus> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.griddynamics.openspaces.convergence.examples.dademo.ui.client.ConvergenceDemoService
     */
    void getConfig(AsyncCallback<StandConfig> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.griddynamics.openspaces.convergence.examples.dademo.ui.client.ConvergenceDemoService
     */
    void ping(AsyncCallback<StandStatus> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.griddynamics.openspaces.convergence.examples.dademo.ui.client.ConvergenceDemoService
     */
    void start(AsyncCallback<StandStatus> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.griddynamics.openspaces.convergence.examples.dademo.ui.client.ConvergenceDemoService
     */
    void stop(AsyncCallback<StandStatus> callback);

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.griddynamics.openspaces.convergence.examples.dademo.ui.client.ConvergenceDemoService
     */
    void setMode(String mode, AsyncCallback<StandStatus> callback);

    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static class Util {

        private static ConvergenceDemoServiceAsync instance;

        public static ConvergenceDemoServiceAsync getInstance() {
            if (instance == null) {
                instance = (ConvergenceDemoServiceAsync) GWT.create(ConvergenceDemoService.class);
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(GWT.getModuleBaseURL() + "ConvergenceDemoService");
            }
            return instance;
        }
    }
}
