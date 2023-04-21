package eq.client;

import eq.client.Managers.DataManager.DataManager;
import eq.client.Managers.NetworkManager.NetworkManager;

/**
 * The main class for the Client.
 */
public class Client {

    /**
	 * The main method.
	 * @param args
	 */
    public static void main(String args[]) {
        DataManager dataManager = new DataManager();
        dataManager.setNetworkManager(new NetworkManager(dataManager));
        dataManager.getAuthenticationFrame().setVisible(true);
    }
}
