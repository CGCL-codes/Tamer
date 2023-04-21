package purej.logging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import purej.context.ApplicationContext;
import purej.context.ApplicationContextFactory;
import purej.logging.application.ApplicationLogger;
import purej.logging.application.LoggerFilter;
import purej.logging.application.LoggerFormatter;
import purej.logging.application.RollingFileHandler;

/**
 * �ΰ� ���丮
 * 
 * <br>
 * ���ø����̼��� �ΰŸ� ��ȯ�Ѵ�.
 * 
 * @author SangBoo Lee
 * 
 * 
 */
public class LoggerFactory {

    /**
     * ���ø����̼� �ΰŸ� ��ȯ�Ѵ�.
     * 
     * @param sourceClassName
     * @param logTypeNumber
     * @return
     */
    public static Logger getLogger(String sourceClassName, int logTypeNumber) {
        return new ApplicationLogger(sourceClassName, logTypeNumber);
    }

    /**
     * ���ø����̼� �ΰŸ� ��ȯ�Ѵ�.
     * 
     * @param sourceClassName
     * @param logTypeNumber
     * @return
     */
    public static Logger getLogger(Class<? extends Object> sourceClass, int logTypeNumber) {
        return new ApplicationLogger(sourceClass.getName(), logTypeNumber);
    }

    /**
     * �ΰŸ� �ʱ�ȭ�Ѵ�.
     * 
     */
    public void initializeLogging() {
        try {
            LogManager manager = LogManager.getLogManager();
            manager.reset();
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LogKey.LOG_NAME);
            for (Handler h : logger.getHandlers()) {
                logger.removeHandler(h);
            }
            for (Handler h : getHandlers()) {
                logger.addHandler(h);
            }
            logger.setUseParentHandlers(false);
            System.out.println("Logger initialized : " + LogKey.LOG_NAME);
        } catch (Exception ex) {
            System.out.println("Logger initialize failed : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     *  �ΰ� �ڵ鷯 ����� ��ȯ�Ѵ�.
     * @return
     * @throws Exception
     */
    private Collection<Handler> getHandlers() throws Exception {
        Collection<Handler> handlers = new ArrayList<Handler>();
        try {
            Vector<Level> acceptableLevels = new Vector<Level>();
            ApplicationContextFactory factory = ApplicationContextFactory.getInstance();
            ApplicationContext appContext = factory.getApplicationContext();
            if (appContext.getAPPLICATION_MODE().equals("DEV")) {
                System.out.println("Logger is [DEV] mode optimized.");
                acceptableLevels.clear();
                acceptableLevels.add(Level.FINEST);
                acceptableLevels.add(Level.FINE);
                acceptableLevels.add(Level.INFO);
                acceptableLevels.add(Level.WARNING);
                acceptableLevels.add(Level.SEVERE);
            } else {
                acceptableLevels.add(Level.WARNING);
                acceptableLevels.add(Level.SEVERE);
            }
            Filter filter = new LoggerFilter(acceptableLevels);
            Formatter formatter = new LoggerFormatter();
            Handler fileHandler = new RollingFileHandler();
            fileHandler.setFilter(filter);
            fileHandler.setFormatter(formatter);
            Handler consoleHandler = new java.util.logging.ConsoleHandler();
            consoleHandler.setFilter(filter);
            consoleHandler.setFormatter(formatter);
            handlers.add(fileHandler);
            return handlers;
        } catch (Exception ex) {
            throw new Exception("Failed logger handler parsing : " + ex.getMessage());
        }
    }
}
