package gjset.client;

import gjset.client.gui.MainFrame;
import gjset.client.gui.pages.LaunchPage;
import gjset.client.gui.pages.ServerConnectPage;
import javax.swing.SwingUtilities;

/**
 * This is the primary controller for the client.  It manages messages
 * across all of the different UI pages and handles changing pages as appropriate.
 */
public class ClientController {

    private MainFrame mainFrame;

    private LaunchPage launchPage;

    private ServerConnectPage userLoginPage;

    private ConcreteClientCommunicator communicator;

    private ConnectionInitializer connectionInitializer;

    public ClientController() {
        mainFrame = new MainFrame();
        createPages();
    }

    /**
	 * Create all of our pages
	 */
    private void createPages() {
        launchPage = new LaunchPage(this);
        userLoginPage = new ServerConnectPage(this);
    }

    /**
	 * Start running the application.
	 */
    public void start() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                mainFrame.loadPage(launchPage);
            }
        });
    }

    /**
	 * Launch a multiplayer game.
	 */
    public void launchMultiplayerGame() {
        communicator = new ConcreteClientCommunicator();
        connectionInitializer = new ConnectionInitializer(communicator);
        connectionInitializer.connectToServer();
        mainFrame.loadPage(userLoginPage);
    }

    /**
	 * Attempt to log in using the indicated username and password.
	 *
	 * @param name
	 * @param password
	 */
    public void attemptLogin(String name, String password) {
        connectionInitializer.authenticateUser(name, password);
    }

    /**
	 * Create a new user account
	 *
	 */
    public void createAccount() {
    }

    /**
	 * Go back one page.
	 *
	 */
    public void backAPage() {
        mainFrame.backAPage();
    }
}
