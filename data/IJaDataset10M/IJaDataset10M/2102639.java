package openogk.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Benjamin
 */
public class Server {

    private boolean run = false;

    private Map<String, Game> gamesMap;

    private Map<String, Table> tablesMap;

    private Registry rmiRegistry;

    public Server(boolean run) throws RemoteException {
        this.gamesMap = new HashMap<String, Game>();
        this.tablesMap = new HashMap<String, Table>();
        this.run = run;
        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        this.rmiRegistry = LocateRegistry.getRegistry();
    }

    public void stop() {
        this.run = false;
    }

    public void registerGame(Game game, String id) throws RemoteException {
        this.gamesMap.put(id, game);
        Game gameStub = (Game) UnicastRemoteObject.exportObject(game, 0);
        RemoteServer.setLog(System.out);
        this.rmiRegistry.rebind(id, gameStub);
        System.out.println("stub " + id + " registered");
    }

    public void runTable(String id) {
    }
}
