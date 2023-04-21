package net.sf.openv4j.swingui;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class DesktopApplication1 extends SingleFrameApplication {

    /**
     * A convenient static getter for the application instance.
     *
     * @return the instance of DesktopApplication1
     */
    public static DesktopApplication1 getApplication() {
        return Application.getInstance(DesktopApplication1.class);
    }

    /**
     * Main method launching the application.
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        launch(DesktopApplication1.class, args);
    }

    /**
     * This method is to initialize the specified window by injecting resources. Windows shown in our application come fully initialized from the GUI builder, so this additional configuration is not needed.
     *
     * @param root DOCUMENT ME!
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new DesktopApplication1View(this));
    }
}
