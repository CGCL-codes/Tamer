package jxsdgenerator;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxsdgenerator.engine.Generator;
import jxsdgenerator.engine.util.Console;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * <p>Title: Yeradis P. Barbosa Marrero</p>
 *
 * <p>Description: A XML binding Compiler </p>
 *
 * <p>Copyright: Copyright (c) 2007 Yeradis P. Barbosa Marrero</p>
 *
 * <p>Company: MyOwn</p>
 *
 * @author Yeradis P. Barbosa Marrero
 * @version 1.0
 */
public class JXSDGeneratorApp extends SingleFrameApplication {

    Console console;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        try {
            console = new Console();
            console.setVisible(true);
            show(new JXSDGeneratorView(this));
        } catch (IOException ex) {
            Logger.getLogger(JXSDGeneratorApp.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     * @return the instance of JXSDGeneratorApp
     */
    public static JXSDGeneratorApp getApplication() {
        return Application.getInstance(JXSDGeneratorApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(JXSDGeneratorApp.class, args);
    }
}
