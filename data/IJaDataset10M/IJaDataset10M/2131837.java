package cx.ath.mancel01.dependencyshot.test.logger;

import cx.ath.mancel01.dependencyshot.utils.annotations.Log;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author Mathieu ANCELIN
 */
public class Module {

    @Inject
    @Log
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    public void logSomething(String log) {
        logger.log(Level.INFO, log);
    }
}
