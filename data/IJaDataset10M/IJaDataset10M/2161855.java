package info.walnutstreet.vs.ps03.client;

import info.walnutstreet.vs.ps03.client.view.MainWindow;
import info.walnutstreet.vs.ps03.exceptions.CompositeAlreadySetupException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import org.apache.log4j.Logger;

/**
 * @author Christpoh Gostner
 * @version 0.1
 *
 */
public class Client {

    /**
	 * Logging ...
	 */
    private static Logger logger = Logger.getLogger(Client.class);

    /**
	 * Main.
	 * 
	 * @param args
	 * @throws IOException IOException
	 * @throws UnknownHostException 
	 * @throws CompositeAlreadySetupException 
	 * @throws RemoteException 
	 * @throws NotBoundException 
	 */
    public static void main(String[] args) throws UnknownHostException, IOException, CompositeAlreadySetupException {
        Client.logger.debug("Client started ...");
        MainWindow window = new MainWindow();
        window.createTabFolder();
        window.createClientListControl();
        window.createGoodComposite();
        if (window.connectionDialog()) window.runWindow();
        Client.logger.debug("Client stopped");
    }
}
