package eu.keep.gui.common;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Class to receive log4j logging events and display them in a (list of) LogPanel(s)
 */
public class Log4jAppender extends WriterAppender {

    private static List<LogPanel> logPanels = new ArrayList<LogPanel>();

    /**
	 *  Add a LogPanel for the logging information to appear.
	 *  @param logPanel panel that will receive the logging information 
	 */
    public static void addTextArea(LogPanel logPanel) {
        Log4jAppender.logPanels.add(logPanel);
    }

    /**
	 * Append a logging message to the LogPanel associated with this Appender
	 * @param loggingEvent the loggingEvent to be logged
	 */
    @Override
    public void append(LoggingEvent loggingEvent) {
        final String message = this.layout.format(loggingEvent);
        for (LogPanel panel : logPanels) {
            panel.logMessage(message);
        }
    }
}
