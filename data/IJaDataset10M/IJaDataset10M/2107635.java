package jadaserver;

import jadacommon.model.attribute.AttributeController;
import jadaserver.data.AccountController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import jadaserver.data.ClassLoader;

/**
 * The main class of the application.
 */
public class JadaServerApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new JadaServerView(this));
        ClassLoader.getInstance();
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of JadaServerApp
     */
    public static JadaServerApp getApplication() {
        return Application.getInstance(JadaServerApp.class);
    }

    @Override
    protected void shutdown() {
        super.shutdown();
        try {
            AccountController.getInstance().saveAccounts();
        } catch (IOException ex) {
            Logger.getLogger(JadaServerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(JadaServerApp.class, args);
    }
}
