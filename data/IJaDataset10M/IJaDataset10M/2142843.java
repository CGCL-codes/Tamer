package ganymede.log4j;

import org.apache.log4j.spi.LoggingEvent;

/**
 * @author Brandon
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Log4jThrowable implements Log4jItem {

    private LoggingEvent le;

    public Log4jThrowable(LoggingEvent event) {
        le = event;
    }

    /**
     * @see ganymede.log4j.Log4jItem#getText()
     */
    public String getText() {
        if (le.getThrowableInformation() != null) {
            return "X";
        } else {
            return "";
        }
    }
}
