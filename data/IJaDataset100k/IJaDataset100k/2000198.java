package bcr.client.util;

import org.jboss.logging.Logger;

public class ClientLogger {

    private static Logger log;

    private static void initialize() {
        try {
            String name = ClientLogger.class.getName();
            log = Logger.getLogger(name);
        } catch (Exception e) {
            System.err.println("Error creating Logger: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public static Logger getInstance() {
        if (log == null) {
            initialize();
        }
        return log;
    }
}
