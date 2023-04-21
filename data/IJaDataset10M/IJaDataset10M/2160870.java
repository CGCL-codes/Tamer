package fitnesse.Fixtures;

import it.unibz.izock.networking.Authenticator;
import it.unibz.izock.networking.Server;
import java.rmi.Naming;
import java.rmi.RemoteException;
import fitlibrary.DoFixture;
import fitnesse.NitFixtures.ClientImpl;
import fitnesse.NitFixtures.GameStates;
import fitnesse.NitFixtures.iZockException;

public class RegisterFixture extends DoFixture {

    public void connectClientToServer(String username, String servername) throws iZockException {
        try {
            Authenticator authenticator = (Authenticator) Naming.lookup(servername);
            ClientImpl client = new ClientImpl();
            Server server = authenticator.login(username, "f561aaf6ef0bf14d4208bb46a4ccb3ad", client);
            client.setServer(server);
            if (server.getGames().size() == 0) {
                server.createGame("Test Game");
            }
            server.getGames().get(0).join(client);
            GameStates.getInstance().setClient(client);
        } catch (Exception e) {
            e.printStackTrace();
            throw new iZockException(e.getMessage());
        }
    }

    public void clientCreatesGame(int client, String name) throws iZockException {
        try {
            GameStates.getInstance().getClients().get(client).getServer().createGame(name);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new iZockException(e.getMessage());
        }
    }
}
