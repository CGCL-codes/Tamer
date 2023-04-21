package emil.poker;

import org.apache.log4j.Logger;

public class LoggableImpl implements Loggable {

    private Logger logger;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }
}
