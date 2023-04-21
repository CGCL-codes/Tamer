package connector;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import match.Match;
import match.MatchEngine;
import exc.RefusedConnectionException;
import gui.RemoteController;

/**
 * @author WormHole Interactive
 * 
 */
public class Connector {

    /**
	 * Creates a new <tt>Match</tt> that extends the class Model. It makes a new
	 * match and shares this through RMI. The new match is returned for the
	 * local player
	 * 
	 * @throws MalformedURLException
	 * 
	 */
    public static Match newMatch(int dim, long periodoTimer) throws RemoteException, MalformedURLException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        Match obj = null;
        obj = new MatchEngine(dim, periodoTimer);
        Naming.rebind("//localhost:1099/match", obj);
        System.out.println("Match e' stato pubblicato");
        return obj;
    }

    /**
	 * Each remote player use this method to instantiate a remote connection
	 * 
	 * @param server
	 *            the master player IP
	 * @return the match to play, if the connection has been accepted
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws RefusedConnectionException
	 *             if this is an exceeding connection
	 */
    public static Match connectMatch(String server) throws MalformedURLException, RemoteException, NotBoundException, RefusedConnectionException {
        Match match = (Match) Naming.lookup("//" + server + ":1099/match");
        if (match.acceptConnections()) return match; else throw new RefusedConnectionException("Unable to allocate free slots to play as a remote player.\n" + "This match has just the correct number of connected players");
    }

    /**
	 * Remove the object from remote server 
	 * 
	 * @param server
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
    public static void unBindMatch(String server) throws RemoteException, MalformedURLException, NotBoundException {
        Naming.unbind("//" + server + ":1099/match");
    }

    /**
	 * Public remote object 
	 * 
	 * @param controller
	 * @param playerName
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
    public static void publishController(RemoteController controller, String playerName) throws RemoteException, MalformedURLException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        String rmiObjName = "//localhost:1099/" + playerName + "_controller";
        Naming.rebind(rmiObjName, controller);
    }

    /**
	 * Download the remote controller from the computer of a player
	 * 
	 * @param server
	 * @param playerName
	 * @return
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
    public static RemoteController retrieveController(String server, String playerName) throws MalformedURLException, RemoteException, NotBoundException {
        return (RemoteController) Naming.lookup("//" + server + ":1099/" + playerName + "_controller");
    }

    /**
	 * Remove the controller published
	 * @param playerName
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
    public static void unPublishController(String playerName) throws RemoteException, MalformedURLException, NotBoundException {
        String rmiObjName = "//localhost/" + playerName + "_controller";
        Naming.unbind(rmiObjName);
    }
}
