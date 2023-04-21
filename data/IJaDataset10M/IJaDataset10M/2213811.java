package snowide;

import java.awt.SplashScreen;
import snowide.ui.SnowIDEView;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class SnowIDEApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new SnowIDEView(this));
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
     * @return the instance of SnowIDEApp
     */
    public static SnowIDEApp getApplication() {
        return Application.getInstance(SnowIDEApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        snowide.ui.SplashScreen s = new snowide.ui.SplashScreen();
        s.showSplash();
        launch(SnowIDEApp.class, args);
        while (true) {
            try {
                Thread.sleep(250);
                if (getApplication().getMainFrame().isVisible()) {
                    break;
                }
            } catch (Exception e) {
            }
        }
        s.hideSplash();
    }
}
