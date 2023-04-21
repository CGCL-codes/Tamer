package com.gampire.pc.net.rmi;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.security.Permission;

public class RMIUtils {

    public static void startupRMI(boolean startupRMIRegistry) throws RemoteException {
        if (startupRMIRegistry) LocateRegistry.createRegistry(1099);
        System.setSecurityManager(new RMISecurityManager() {

            public void checkConnect(String host, int port) {
            }

            public void checkConnect(String host, int port, Object context) {
            }

            public void checkPermission(Permission perm) {
            }
        });
    }

    public static void addRemoteInterFaceToRMICodeBase(Class<?> remoteInterfaceClass) throws RemoteException {
        System.setProperty("java.rmi.server.codebase", remoteInterfaceClass.getProtectionDomain().getCodeSource().getLocation().toString());
    }

    public static void rebindRemote(String name, Remote remote) throws RemoteException, MalformedURLException {
        try {
            Naming.unbind(name);
        } catch (NotBoundException e) {
        }
        try {
            Naming.bind(name, remote);
        } catch (AlreadyBoundException e) {
        }
    }

    public static Remote startupRMIClient(InetAddress rmiServerAddress, String serverRegistryName) throws MalformedURLException, RemoteException, NotBoundException {
        String lookupString = "rmi:/" + rmiServerAddress + "/" + serverRegistryName;
        return Naming.lookup(lookupString);
    }
}
