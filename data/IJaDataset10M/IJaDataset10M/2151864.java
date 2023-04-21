package POSTApp;

import Store.Store;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.SwingUtilities;

/**
 * Main class of the POST GUI application.
 * @author Rebecca Bettencourt
 */
public class POSTApplication {

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            final String name = "Store";
            final Registry registry = LocateRegistry.getRegistry(args[0]);
            final Store s = (Store) registry.lookup(name);
            final POSTController controller = new POSTController(s, s.getProductCatalog());
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    POSTFrame f = new POSTFrame();
                    f.setController(controller);
                    f.setLocationRelativeTo(null);
                    f.setVisible(true);
                }
            });
        } catch (Exception e) {
            System.err.println("Could not get store!");
            e.printStackTrace();
        }
    }
}
