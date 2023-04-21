package com.lts.highlander;

import javax.swing.JOptionPane;
import com.lts.application.Application;
import com.lts.application.ApplicationException;
import com.lts.application.data.ApplicationData;

public class SemaphoreApplication extends Application {

    @Override
    public ApplicationData createApplicationData() throws ApplicationException {
        return null;
    }

    @Override
    public String getApplicationName() {
        return "SemaphoreApplication";
    }

    @Override
    public void startApplication() throws ApplicationException {
        try {
            String s = "control";
            if (getCommandLineArguments().length > 0) {
                s = getCommandLineArguments()[0];
            }
            if (s.equalsIgnoreCase("control")) {
                ControlWindow.launch();
            } else if (s.equalsIgnoreCase("client")) {
                ClientWindow.launch();
            } else {
                JOptionPane.showMessageDialog(null, "unknown");
            }
        } catch (Exception e) {
            showException(e);
        }
    }

    public static void main(String[] argv) {
        SemaphoreApplication app = new SemaphoreApplication();
        app.startApplication(argv);
    }
}
