package net.sourceforge.cruisecontrol.jmx;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Implementation for the LoggerControllerMBean to control the Level used
 * by the controlled Logger.
 *
 * @author <a href="mailto:joriskuipers@xs4all.nl">Joris Kuipers</a>
 *
 */
public class LoggerController implements LoggerControllerMBean {

    private final Logger logger;

    public LoggerController(Logger logger) {
        this.logger = logger;
    }

    /**
     * @see net.sourceforge.cruisecontrol.jmx.LoggerControllerMBean#getLoggingLevel()
     */
    public String getLoggingLevel() {
        return logger.getLevel().toString();
    }

    /**
     * @see net.sourceforge.cruisecontrol.jmx.LoggerControllerMBean#setLoggingLevel(java.lang.String)
     */
    public void setLoggingLevel(String level) {
        logger.setLevel(Level.toLevel(level));
    }

    /**
     * @see net.sourceforge.cruisecontrol.jmx.LoggerControllerMBean#getName()
     */
    public String getName() {
        return logger.getName();
    }
}
