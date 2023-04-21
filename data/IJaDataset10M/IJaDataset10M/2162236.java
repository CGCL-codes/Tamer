package edu.sfsu.powerrangers.jeopardy.audience;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import edu.sfsu.powerrangers.jeopardy.AudienceClient;
import edu.sfsu.powerrangers.jeopardy.AudienceServer;

public class AudienceApplication {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String rmiServer = "668.tesladethray.com";
        String rmiName = "wtq";
        AudienceClient client = null;
        Registry registry = LocateRegistry.getRegistry(rmiServer);
        AudienceServer server = (AudienceServer) registry.lookup(rmiName);
        client = (AudienceClient) UnicastRemoteObject.exportObject(client, 0);
        server.loginAudience(client);
    }
}
