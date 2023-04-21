package com.flaptor.util.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import junit.extensions.TestSetup;
import junit.framework.Test;

/**
 * Test for Rmi
 */
public class RmiTestSetup extends TestSetup {

    public static final String REGISTRY_HOSTNAME = "localhost";

    public static final int REGISTRY_PORT = 1110;

    public static final String REMOTE_SERVICE_NAME = "RemoteEcho";

    private Registry registry = null;

    private IRmiEchoService echoService = null;

    public RmiTestSetup(Test suite) {
        super(suite);
    }

    public void setUp() throws RemoteException {
        echoService = new RmiEchoService();
        registry = LocateRegistry.createRegistry(RmiTestSetup.REGISTRY_PORT);
        registry.rebind(RmiTestSetup.REMOTE_SERVICE_NAME, UnicastRemoteObject.exportObject(echoService, 0));
    }

    public void tearDown() {
    }
}
