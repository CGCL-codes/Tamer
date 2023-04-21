package newgen.presentation.component;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author siddartha
 */
public class NGLLogger {

    private static NGLLogger thisInstance;

    private Logger logger;

    private NGLLogger() {
        try {
            SimpleFormatter sf = new SimpleFormatter();
            logger = Logger.getLogger("com.verus.ngl.presentation");
            FileHandler fh = new FileHandler("%h/NGLPresentation%g.log", true);
            fh.setFormatter(sf);
            logger.addHandler(fh);
        } catch (Exception ex) {
        }
    }

    public static NGLLogger getInstance() {
        if (thisInstance == null) thisInstance = new NGLLogger();
        return thisInstance;
    }

    public Logger getLogger() {
        return logger;
    }
}
