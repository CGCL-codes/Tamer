package uk.ac.ebi.intact.view.webapp.util;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.springframework.stereotype.Component;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: Log4jMBean.java 13518 2009-07-31 09:37:34Z brunoaranda $
 */
@Component
public class Log4jMBean {

    public void activateInfo(String category) {
        LogManager.getLogger(category).setLevel(Level.INFO);
    }

    public void activateDebug(String category) {
        LogManager.getLogger(category).setLevel(Level.DEBUG);
    }

    public void activateWarn(String category) {
        LogManager.getLogger(category).setLevel(Level.WARN);
    }

    public void activateError(String category) {
        LogManager.getLogger(category).setLevel(Level.ERROR);
    }

    public void activateFatal(String category) {
        LogManager.getLogger(category).setLevel(Level.FATAL);
    }
}
