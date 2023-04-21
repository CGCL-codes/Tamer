package com.anothergtdapp.main;

import com.anothergtdapp.view.AnotherGTDView;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.apache.log4j.PropertyConfigurator;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class AnotherGTDApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new AnotherGTDView(this));
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
     * @return the instance of AnotherGTDApp
     */
    public static AnotherGTDApp getApplication() {
        return Application.getInstance(AnotherGTDApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        Properties logProperties = new Properties();
        try {
            logProperties.load(new FileInputStream("log4j.properties"));
            PropertyConfigurator.configure(logProperties);
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load logging property ");
        }
        launch(AnotherGTDApp.class, args);
    }
}
