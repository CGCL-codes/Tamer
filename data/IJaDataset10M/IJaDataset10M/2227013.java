package net.wsnware.platform.tinyos2.jio;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.tinyos.util.Messenger;

/**
 *
 * @author alex
 */
public class LoggerMessenger implements Messenger {

    protected final Logger logger;

    public LoggerMessenger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void message(String s) {
        logger.log(Level.INFO, s);
    }
}
