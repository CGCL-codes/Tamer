package grocerylister;

import grocerylister.ui.GrocerylisterView;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * The main class of the application.
 */
public class GrocerylisterApp extends SingleFrameApplication {

    private static final Logger logger = Logger.getLogger(GrocerylisterApp.class);

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new GrocerylisterView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     * @param root
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of GrocerylisterApp
     */
    public static GrocerylisterApp getApplication() {
        return Application.getInstance(GrocerylisterApp.class);
    }

    /**
     * Main method launching the application.
     * @param args
     */
    public static void main(String[] args) {
        PropertyConfigurator.configureAndWatch("./conf/grocerylister.logging.conf");
        logger.info(java.util.ResourceBundle.getBundle("grocerylister/resources/GrocerylisterApp").getString("log.app.launching"));
        launch(GrocerylisterApp.class, args);
    }

    /**
     * This method adds the appropriate <code>InputMap</code> and
     * <code>ActionMap</code> to a JDialog so that the escape key may be used
     * to cancel the dialog.
     *
     * @param dlg the <code>JDialog</code> for which the escape key will be
     * used to cancel
     * @since 1.0
     */
    public static void setupEscapeKey(final JDialog dlg) {
        JRootPane rootPane = dlg.getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        ActionMap actionMap = rootPane.getActionMap();
        actionMap.put("escape", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dlg.dispose();
            }
        });
    }
}
