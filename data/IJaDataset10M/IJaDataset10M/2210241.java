package eulergui.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import n3_project.ProjectGUI;
import net.sf.parser4j.parser.service.ParserLogger;
import eulergui.EulerGUI;

/**
 * @author Jean-Marc Vanel jeanmarc.vanel@gmail.com
 *
 */
public class LoggingHelper {

    private LoggingHelper() {
    }

    /** For console application (N3 shell == BasicRuntime),
	 * set the logging level of the ConsoleHandler to be set to Level.SEVERE ;
	 * for less stripped output , see {@link EulerGUI} constructor
	 * also log settings in {@link ProjectGUI#main(String[])} */
    public static void setLoggingSevere() {
        try {
            org.apache.log4j.Logger.getLogger(ParserLogger.class).setLevel(org.apache.log4j.Level.FATAL);
            final Handler[] handlers = Logger.getLogger("").getHandlers();
            boolean foundConsoleHandler = false;
            for (int index = 0; index < handlers.length; index++) {
                if (handlers[index] instanceof ConsoleHandler) {
                    handlers[index].setLevel(Level.SEVERE);
                    handlers[index].setFormatter(new SimpleFormatter());
                    foundConsoleHandler = true;
                }
            }
            if (!foundConsoleHandler) {
                System.err.println("No consoleHandler found, adding one.");
                final ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.SEVERE);
                consoleHandler.setFormatter(new SimpleFormatter());
                Logger.getLogger("").addHandler(consoleHandler);
            }
        } catch (final Throwable t) {
            System.err.println("Unexpected Error setting up logging\n" + t);
        }
    }
}
