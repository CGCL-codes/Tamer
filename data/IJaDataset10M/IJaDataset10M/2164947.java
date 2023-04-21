package net.sf.traser;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.xml.namespace.QName;
import net.sf.traser.configuration.AbstractConfigurable;
import org.apache.axiom.om.OMElement;

/**
 *
 * @author Marcell Szathmári
 */
public class Traser extends AbstractConfigurable {

    /**
     * This field is used to LOG messages.
     */
    private static final Logger LOG = Logger.getLogger(Traser.class.getPackage().getName());

    /**
     * Default level of logging, if not specified in configuration.
     */
    private static final Level DEFAULT_LOG_LEVEL = Level.WARNING;

    /**
     * The node name of the configuration entry for specifying the log level.
     */
    private static final QName LOG_LEVEL_ENTRY = new QName("log-level");

    /**
     * The node name of the configuration entry for specifying log files.
     */
    private static final QName LOG_FILE = new QName("log-file");

    /**
     * Sets the level of logging to <code>level</code>.
     * @param level the new level of logging.
     */
    public static void setLogLevel(final String level) {
        LOG.setLevel(Level.parse(level));
    }

    /**
     * Closes all LOG handlers.
     */
    public static void closeAllHandlers() {
        Handler[] hs = LOG.getHandlers();
        for (int i = 0; i < hs.length; ++i) {
            closeHandler(hs[i]);
        }
    }

    /**
     * Closes the LOG handler <code>h</code>.
     * @param handler the handler to close.
     */
    private static void closeHandler(Handler handler) {
        handler.flush();
        handler.close();
        LOG.removeHandler(handler);
    }

    /**
     * This method adds a new file to the set of files the log is written into.
     *
     * @param fileName name of file to write logs into.
     * @throws RuntimeException 
     */
    public static void addLogFile(String fileName) {
        if (fileName == null) {
            return;
        }
        try {
            FileHandler h = new FileHandler(fileName, true);
            h.setFormatter(new SimpleFormatter());
            LOG.addHandler(h);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure() {
        OMElement file;
        Iterator<?> iter = configuration.getChildrenWithName(LOG_FILE);
        while (iter.hasNext()) {
            file = (OMElement) iter.next();
            addLogFile(file.getText());
        }
        Level l = null;
        try {
            l = Level.parse(configuration.getFirstChildWithName(LOG_LEVEL_ENTRY).getText());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            l = DEFAULT_LOG_LEVEL;
        } finally {
            LOG.setLevel(l);
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(l);
        }
        for (Handler h : Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getHandlers()) {
            System.out.println("Handler: " + h.getClass().getName());
        }
        Logger.getLogger("org.apache.xml.security").setLevel(Level.WARNING);
        Logger.getLogger("org.apache.axis2.deployment").setLevel(Level.WARNING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finish() {
        closeAllHandlers();
    }
}
