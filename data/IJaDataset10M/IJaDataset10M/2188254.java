package ru.ispras.texterra.demo.twitter.sigir;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SIGIR {

    public static final String rmiSigirName = "IndexWordIds";

    public static final String IndexWordIdsHostname = SettingsManager.getOption("server_machine");

    public static int registryPort = Integer.parseInt(SettingsManager.getOption("rmi_port"));

    public static int indexWordsIdsPort = Integer.parseInt(SettingsManager.getOption("server_port"));

    private static IndexWordIdsInterface stub = null;

    private static Registry registry;

    public static boolean shoultReconnect = true;

    public static IndexWordIdsInterface getIndexWordIdsObject() throws RemoteException, NotBoundException {
        if ((stub == null) || shoultReconnect) {
            registry = LocateRegistry.getRegistry(SIGIR.IndexWordIdsHostname, registryPort);
            stub = (IndexWordIdsInterface) registry.lookup(SIGIR.rmiSigirName);
            shoultReconnect = false;
        }
        return stub;
    }
}
