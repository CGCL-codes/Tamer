package wisi;

import logging.AppendManager;
import logging.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class WiSiApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        Logger.configure();
        AppendManager.configure();
        Logger.info("WiSiApp Startup");
        WiSiView w = new WiSiView(this);
        show(w);
        w.showLogin();
        Logger.info("WiSiView visible");
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
     * @return the instance of WiSiApp
     */
    public static WiSiApp getApplication() {
        return Application.getInstance(WiSiApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(WiSiApp.class, args);
    }
}
