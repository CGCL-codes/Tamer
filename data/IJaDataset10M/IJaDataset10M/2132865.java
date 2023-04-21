package dbaccess.util2;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.*;

/**
 * Initializes and configures logging using log4j.  This class uses pre-defined
 * properties to allow the configuring to be customized at run time.  These
 * properties can be supplied individually or through the <i>DBProperties</i>
 * object.
 * <p>
 * Normally, you would configure log4j properties using a log4j.properties
 * file which would provide far greater flexibility.  However, since some
 * programs may potentially be used by the general scientific community who
 * may not be familiar with configuring log4j, this class will configure
 * log4j using defaults and allow some simple overrides for more flexibility.
 * <p> 
 * Log4j properties can be supplied on the command line or in a log4j
 * configuration file, which by default is located in <i>/etc/spidr</i>
 * (This location can be over-ridden on the command line).  If defined on the
 * command line they must be supplied in the form <i>--name=value</i>:
 * <p>
 * <table>
 * <tr><td><b>--logcfg</b></td>    <td>Qualified name of a log4j.properties file</td></tr>
 * <tr><td><b>--loglevel</b></td>  <td>Logging level</td></tr>
 * <tr><td><b>--logpattern</b></td><td>Logging display pattern</td></tr>
 * <tr><td><b>--logfile</b></td>   <td>Logging output file</td></tr>
 * </table>
 * <p>
 * The command line options are parsed by and stored in a object of the
 * {@link dbaccess.util2.DBProperties DBProperties} class (which extends the
 * {@link java.util.Properties java.util.Properties} class).
 * <p>
 * The <i>DBProperties</i> class supports short-cut forms for these
 * logging properties.
 * <p>
 * These shortcuts can be used on the command line in the form <i>-flag value</i>:
 * <p>
 * <table>
 * <tr><td><b> -C</b></td> <td>Same as --logcfg</td></tr>
 * <tr><td><b> -L</b></td> <td>Same as --loglevel</td></tr>
 * <tr><td><b> -P</b></td> <td>Same as --logpattern</td></tr>
 * <tr><td><b> -F</b></td> <td>Same as --logfile</td></tr>
 * </table>
 * <p>
 * These shortcuts can be used on the command line to set the log4j logging
 * level and must be in the form <i>--flag</i>:
 * <p>
 * <table>
 * <tr><td><b>--fatal</b></td><td>Set level to FATAL</td></tr>
 * <tr><td><b>--error</b></td><td>Set level to ERROR</td></tr>
 * <tr><td><b>--warn</b></td> <td>Set level to WARN</td></tr>
 * <tr><td><b>--info</b></td> <td>Set level to INFO</td></tr>
 * <tr><td><b>--debug</b></td><td>Set level to DEBUG</td></tr>
 * <tr><td><b>--none</b></td> <td>Set level to NONE (turn off logging</td></tr>
 * <tr><td><b>--all</b></td>  <td>Set level to ALL (log all levels)</td></tr>
 * </table>
 * <p>
 * The sequence this class uses to configure the log4j system is outlined here:
 * <ul>
 *  <li>Check to see if logging has already been configured using a log4j.properties file.  If so, then do nothing and return.
 *  <li>Otherwise, configure logging:
 *   <ol>
 *   <li>If the <b>--logcfg=<i>logcfgFile</i></b> property has been defined
 * then the logging is controlled by the properties defined in the 
 * <i>logcfgFile</i> file. 
 *   <li>Otherwise, if the <b>--logfile=<i>logFile</i></b> property is defined
 * logging messages are directed to the file named.
 *   <li>If neither the <b>--logcfg</b> or <b>--logfile</b> properties are defined logging messages are directed to stdout.
 *   <li>The logging level defaults to "INFO" but you can override this with
 * the <b>--loglevel=<i>level</i></b> property.  The level is one of the
 * log4j standard levels "DEBUG","INFO", "WARN", "ERROR", "FATAL", etc.,
 * which can be specified in with upper or lower case or abbreviated with
 * just the first letter of the level.
 *   <li>The format of logging messages are also defaulted but can also be
 * over-ridden using the <b>--logpattern=<i>pattern</i></b> property.  The
 * <i>pattern</i> is a log4j pattern string.
 *   </ol>
 * </ul>
 */
public class Logging {

    private DBProperties prop;

    private String logcfg;

    private String logfile;

    private String logpattern;

    private String loglevel;

    private static final String LOG_LEVEL = "INFO";

    private static final String LOG_PATTERN = "%d{MM/dd/yy HH:mm:ss} %-5p %c{2} - %m%n";

    private static Log4JLogger l4j = new Log4JLogger("Logging");

    private static Logger logger = l4j.getLogger();

    /**
    * Constructor
    */
    public Logging() {
        this.prop = new DBProperties();
        if (!initialize()) System.exit(1);
    }

    /**
    * Constructor. Set the program properties and initialize.
    * @param p The properties parsed from the command line and property files.
    */
    public Logging(DBProperties p) {
        this.prop = p;
        if (!initialize()) System.exit(1);
    }

    /**
    * Set the program properties.
    * @param p DBProperties
    */
    public void setProperties(DBProperties p) {
        this.prop = p;
    }

    /**
    * Initialize logging.
    * @param p DBProperties
    * @return true if logging initialized successfully; false on failure.
    */
    public boolean initialize(DBProperties p) {
        this.prop = p;
        return initialize();
    }

    /**
    * Initialize logging.
    * @return true if logging initialized successfully; false on failure.
    */
    public boolean initialize() {
        Logger root = Logger.getRootLogger();
        Enumeration appender = root.getAllAppenders();
        if (appender.hasMoreElements()) return true;
        logcfg = prop.getProperty("logcfg", null);
        logfile = prop.getProperty("logfile", null);
        logpattern = prop.getProperty("logpattern", LOG_PATTERN);
        if (logcfg == null) {
            PatternLayout pl = new PatternLayout(logpattern);
            if (logfile == null) {
                ConsoleAppender ca = new ConsoleAppender(pl);
                BasicConfigurator.configure(ca);
            } else {
                try {
                    FileAppender fa = new FileAppender(pl, logfile, true);
                    BasicConfigurator.configure(fa);
                } catch (FileNotFoundException e) {
                    ConsoleAppender ca = new ConsoleAppender(pl);
                    BasicConfigurator.configure(ca);
                    logger.warn("Log file '" + logfile + "' not found. Logging to console");
                } catch (Exception e) {
                    System.err.println("***ERROR: Could not set up logging: " + e.getMessage());
                    return false;
                }
            }
        } else {
            PropertyConfigurator.configure(logcfg);
        }
        loglevel = prop.getProperty("loglevel", LOG_LEVEL);
        logger.setLevel(Level.toLevel(loglevel));
        Logger rootlogger = logger.getRootLogger();
        rootlogger.setLevel(Level.toLevel(loglevel));
        return true;
    }

    /**
    * Get logging parameters and format as a single string that can
    * be displayed.
    * @return A string describing the logging parameters used.
    */
    public String getConfigInfo() {
        String info = "\nLog4j Configuration:";
        info += "\n  Log level=" + loglevel;
        if (logcfg != null) {
            info += "\n  Log configuration file=" + logcfg;
        } else {
            if (logfile != null) info += "\n  Log output file=" + logfile;
            if (logpattern != null) info += "\n  Log pattern=" + logpattern;
        }
        return info;
    }
}
