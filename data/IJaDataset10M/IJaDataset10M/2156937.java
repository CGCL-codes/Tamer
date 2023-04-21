package org.myrpg.holy_factory;

import org.myrpg.holy_factory.gui.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HolyFactory implements Loggable {

    MainWindow mainWindow;

    Project project;

    /**
	 * Main method of the application.
	 *
	 * @param	argv	an array of arguments.
	 */
    public static void main(String[] argv) {
        HolyFactory holyFactory;
        holyFactory = null;
        try {
            holyFactory = new HolyFactory();
            holyFactory.log("Running Holy Factory...");
            holyFactory.mainWindow = new MainWindow(holyFactory);
            holyFactory.mainWindow.setVisible(true);
        } catch (Throwable x) {
            if (holyFactory != null) {
                holyFactory.log("HiCare Error");
                ExceptionLogger.log(x, holyFactory);
            } else {
                System.out.println("Holy Factory Error");
                x.printStackTrace(System.out);
            }
        }
    }

    public HolyFactory() {
        project = null;
    }

    /**
	 * Prints a message on the standard output.
	 *
	 * @param	message	the message to be displayed.
	 */
    public void log(String message) {
        System.out.println(message);
    }

    public void exitSystem() {
        log("Exiting Holy Factory...");
        System.exit(0);
    }
}
