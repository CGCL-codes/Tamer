package be.vds.jtbdive.client.launch;

import be.vds.jtb.basics.client.application.AbstractApplication;
import be.vds.jtb.basics.client.application.Application;

public class LogBookApplication extends AbstractApplication {

    private static Application instance;

    private LogBookApplication() {
        super("logbook");
    }

    public static Application getInstance() {
        if (null == instance) {
            instance = new LogBookApplication();
        }
        return instance;
    }
}
