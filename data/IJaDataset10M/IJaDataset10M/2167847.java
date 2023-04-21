package edu.unibi.agbi.biodwh.config.log;

import java.io.IOException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.xml.DOMConfigurator;
import edu.unibi.agbi.biodwh.config.BioDWHSettings;
import edu.unibi.agbi.biodwh.config.log.database.LogHibernateAppender;

/**
 * @author Benjamin Kormeier
 * @version 1.00 05.07.2008
 */
public class ConfigureLog4j {

    private static final PatternLayout LAYOUT = new PatternLayout("%d{ISO8601} %-5p [%t] %c: %m%n");

    private static final String DEFAULT_MAX_FILE_SIZE = new String("12MB");

    private static String biodwh_logfile = new String("logfiles/biodwh.log");

    private static String config_logfile = new String("logfiles/config.log");

    private static String parser_logfile = new String("logfiles/parser.log");

    private static String download_logfile = new String("logfiles/download.log");

    private static String project_logfile = new String("logfiles/project.log");

    private static String commons_logfile = new String("logfiles/thirdparty/apache_commons.log");

    private static String hibernate_logfile = new String("logfiles/thirdparty/hibernate.log");

    private static String psidev_logfile = new String("logfiles/thirdparty/psidev.log");

    private static Logger biodwh_logger = Logger.getLogger("edu.unibi.agbi.biodwh");

    private static Logger config_logger = Logger.getLogger("edu.unibi.agbi.biodwh.config");

    private static Logger parser_logger = Logger.getLogger("edu.unibi.agbi.biodwh.parser");

    private static Logger download_logger = Logger.getLogger("edu.unibi.agbi.biodwh.download");

    private static Logger project_logger = Logger.getLogger("edu.unibi.agbi.biodwh.project");

    private static Logger commons_logger = Logger.getLogger("org.apache.commons");

    private static Logger hibernate_logger = Logger.getLogger("org.hibernate");

    private static Logger mint_psidev_logger = Logger.getLogger("mint.psidev.psi.mi.xml");

    private static Logger intact_psidev_logger = Logger.getLogger("psidev.psi.mi.xml");

    private static LogHibernateAppender hibernate_appender = new LogHibernateAppender();

    /**
	 * Default configuration for file logging using file appender.
	 * @param logger
	 * @param logfile
	 * @param maxFileSize
	 * @param level
	 * @throws IOException
	 */
    private static void configureFileLogging(Logger logger, String logfile, String maxFileSize, Level level) throws IOException {
        RollingFileAppender appender = new RollingFileAppender(LAYOUT, logfile, true);
        appender.setMaxFileSize(maxFileSize);
        logger.setLevel(level);
        logger.addAppender(appender);
    }

    /**
	 * Default configuration for console logging.
	 * @param logger
	 * @param level
	 */
    private static void configureConsoleLogging(Logger logger, Level level) {
        ConsoleAppender appender = new ConsoleAppender(LAYOUT);
        logger.setLevel(level);
        logger.addAppender(appender);
    }

    public static void enableDatabaseLogging(boolean enable) {
        if (enable) {
            removeDatabaseLogger();
            configureDatabaseLogging(biodwh_logger, Level.INFO);
            configureDatabaseLogging(commons_logger, Level.WARN);
            configureDatabaseLogging(hibernate_logger, Level.WARN);
            configureDatabaseLogging(mint_psidev_logger, Level.WARN);
            configureDatabaseLogging(intact_psidev_logger, Level.WARN);
        } else removeDatabaseLogger();
    }

    protected static void configureDatabaseLogging(Logger logger, Level level) {
        hibernate_appender.setDatabaseConnection(BioDWHSettings.getLogDatabaseServer(), BioDWHSettings.getLogDatabasePort(), BioDWHSettings.getLogDatabaseDB(), BioDWHSettings.getLogDatabaseUsername(), BioDWHSettings.getLogDatabasePassword(), BioDWHSettings.getLogDatabaseDialect());
        logger.setLevel(level);
        logger.addAppender(hibernate_appender);
    }

    protected static void removeDatabaseLogger() {
        biodwh_logger.removeAppender(hibernate_appender);
        commons_logger.removeAppender(hibernate_appender);
        hibernate_logger.removeAppender(hibernate_appender);
        mint_psidev_logger.removeAppender(hibernate_appender);
        intact_psidev_logger.removeAppender(hibernate_appender);
    }

    /**
	 * Default logging configuration for BioDWH. Including logger for Apache Axis, Apache Axiom,
	 * Apache Commons and Apache HTTP Client.
	 * @param log2file - If <b>true</b> all log information will be stored in the <i>logfiles</i> directory.<br>
	 * Else console logging is activated.
	 * @throws IOException
	 */
    public static void defaultLogging(boolean log2file) throws IOException {
        if (log2file) {
            ConfigureLog4j.configureFileLogging(biodwh_logger, biodwh_logfile, DEFAULT_MAX_FILE_SIZE, Level.INFO);
            ConfigureLog4j.configureFileLogging(config_logger, config_logfile, DEFAULT_MAX_FILE_SIZE, Level.WARN);
            ConfigureLog4j.configureFileLogging(parser_logger, parser_logfile, DEFAULT_MAX_FILE_SIZE, Level.INFO);
            ConfigureLog4j.configureFileLogging(download_logger, download_logfile, DEFAULT_MAX_FILE_SIZE, Level.WARN);
            ConfigureLog4j.configureFileLogging(project_logger, project_logfile, DEFAULT_MAX_FILE_SIZE, Level.WARN);
            ConfigureLog4j.configureFileLogging(commons_logger, commons_logfile, DEFAULT_MAX_FILE_SIZE, Level.WARN);
            ConfigureLog4j.configureFileLogging(hibernate_logger, hibernate_logfile, DEFAULT_MAX_FILE_SIZE, Level.WARN);
            ConfigureLog4j.configureFileLogging(mint_psidev_logger, psidev_logfile, DEFAULT_MAX_FILE_SIZE, Level.WARN);
            ConfigureLog4j.configureFileLogging(intact_psidev_logger, psidev_logfile, DEFAULT_MAX_FILE_SIZE, Level.WARN);
        } else {
            ConfigureLog4j.configureConsoleLogging(biodwh_logger, Level.INFO);
            ConfigureLog4j.configureConsoleLogging(commons_logger, Level.WARN);
            ConfigureLog4j.configureConsoleLogging(hibernate_logger, Level.WARN);
            ConfigureLog4j.configureConsoleLogging(mint_psidev_logger, Level.WARN);
            ConfigureLog4j.configureConsoleLogging(intact_psidev_logger, Level.WARN);
        }
    }

    /**
	 * Load log4j XML configuration from given file.
	 * @param file XML - configuration file.
	 */
    public static void loadLog4jXMLConfiguration(String file) {
        DOMConfigurator.configure(file);
    }
}
