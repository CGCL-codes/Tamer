package scamsoft.squadleader.server;

import scamsoft.squadleader.rules.InfantryGroup;
import scamsoft.squadleader.rules.Order;
import scamsoft.squadleader.rules.Player;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameRemote extends Remote {

    void doAction(Order a) throws RemoteException;

    List getRoutPaths(InfantryGroup routers, Player player) throws RemoteException;

    /**
     * Get the name of the scenario
     *
     * @return
     * @throws RemoteException
     */
    String getScenarioName() throws RemoteException;
}
