package org.personalsmartspace.pss_2_ipojo_Gui.app;

import java.util.EventObject;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.personalsmartspace.pss_2_ipojo_Gui.ui.Pss_2_ipojo_GuiTestHarness;

/**
 * The main class of the application.
 */
public class Pss_2_ipojo_GuiApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        this.addExitListener(new ConfirmExit());
        show(new Pss_2_ipojo_GuiTestHarness(this));
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
     * @return the instance of ServiceBrowserApp
     */
    public static Pss_2_ipojo_GuiApp getApplication() {
        return Application.getInstance(Pss_2_ipojo_GuiApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(Pss_2_ipojo_GuiApp.class, args);
    }

    class ConfirmExit implements Application.ExitListener {

        public boolean canExit(EventObject e) {
            Object source = (e != null) ? e.getSource() : null;
            JOptionPane.showMessageDialog(null, "Shutdown option not available - container shutdown required");
            return false;
        }

        public void willExit(EventObject e) {
        }
    }
}
