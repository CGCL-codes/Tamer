package be.vds.jtbdive.core.logging;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

/**
 * Logger Classe utilitaire de gestion du logging, sur base de l'implementation
 * de Apache Log4J. Cette classe assure une certaine independance entre
 * l'application et le logger Log4J, permettant de minimiser l'impact d'une
 * migration eventuelle vers le logger standard du JDK 1.4 Sous log4j, une
 * configuration de logging particuliere est definie en terme de categorie (par
 * defaut, la classe appelante), de priorite (DEBUG..FATAL) et d'appender
 * (destination). Les categories sont hierarchiques, sous la categorie de base,
 * ROOT.
 * 
 * @author clm
 * @version 1.0
 */
public class Syslog implements Serializable {

    private static final long serialVersionUID = -1399846107564648783L;

    /** Nom de la categorie (cfr log4j) */
    private final String name;

    /** logger delegue log4j */
    private Logger log;

    /** pattern par default */
    private static final String DEFAULT_PATTERN = "%d{yyyy-MM-dd HH:mm} %6p [%c{1}] %m%n";

    /** priorites, projetees sur les priorites standards de log4j */
    private static final Level DEBUG = Level.DEBUG;

    private static final Level INFO = Level.INFO;

    private static final Level WARN = Level.WARN;

    private static final Level ERROR = Level.ERROR;

    private static final Level FATAL = Level.FATAL;

    public static final int INT_DEBUG = DEBUG.toInt();

    public static final int INT_INFO = INFO.toInt();

    public static final int INT_WARN = WARN.toInt();

    public static final int INT_ERROR = ERROR.toInt();

    public static final int INT_FATAL = FATAL.toInt();

    public static final Integer[] levels = new Integer[] { INT_DEBUG, INT_INFO, INT_WARN, INT_ERROR, INT_FATAL };

    private static final String LISTENER_APPANDER = "CONSOLE";

    /**
	 * Logger(name) Createur de base, avec un nom de categorie
	 * 
	 * @param name
	 *            nom de la categorie.
	 */
    protected Syslog(final String name) {
        this.name = name;
        log = Logger.getLogger(name);
    }

    /**
	 * Logger() Createur renvoyant une instance de la categorie ROOT
	 * 
	 * @param None
	 */
    protected Syslog() {
        this.name = "ROOT";
        log = Logger.getRootLogger();
    }

    /**
	 * getCategory() Exposition de la categorie log4j du logger
	 */
    public Logger getLoggerImpl() {
        return log;
    }

    /**
	 * getName() Renvoie le nom de la categorie du logger
	 * 
	 * @return Categorie.
	 */
    public String getName() {
        return name;
    }

    /**
	 * configure(props) Configuration du logger a partir de proprietes lues d'un
	 * fichier log4j.properties
	 * 
	 * @param props
	 *            Properties
	 */
    public void configure(Properties props) {
        if (props != null) {
            PropertyConfigurator.configure(props);
        }
    }

    /**
	 * setConsole(bSet) Instanciation simple d'un appender 'Console' au logger
	 * 
	 * @param bSet
	 *            true | false
	 */
    public void setConsole(boolean bSet) {
        if (bSet) {
            log.removeAppender("stdout");
            ConsoleAppender c = new ConsoleAppender(new PatternLayout(DEFAULT_PATTERN));
            c.setName("console");
            log.addAppender(c);
        } else {
            log.removeAppender("console");
        }
    }

    /**
	 * setLogFile(fileName) Instanciation simple d'un appender fichier au logger
	 * 
	 * @param fileName
	 *            Nom du fichier de log
	 */
    public void setLogFile(String fileName) {
        if (fileName != null) {
            try {
                FileAppender f = new FileAppender(new PatternLayout(DEFAULT_PATTERN), fileName);
                f.setName("file");
                log.addAppender(f);
            } catch (IOException e1) {
            }
        } else {
            log.removeAppender("file");
        }
    }

    /**
	 * _getLogger() Internal method to access attribute log
	 * 
	 */
    private Logger _getLogger() {
        if (log == null) {
            if (name == null || name.equals("ROOT")) log = Logger.getRootLogger(); else log = Logger.getLogger(name);
        }
        return log;
    }

    /**
	 * setLevel(Level p) Instanciation d'une priorite donnee au logger
	 * 
	 * @param p
	 *            Priorite (DEBUG, WARN, INFO, ERROR, FATAL)
	 */
    public void setLevel(Level p) {
        _getLogger().setLevel(p);
    }

    public void setLevel(int level) {
        if (level == INT_DEBUG) {
            setLevel(DEBUG);
            debug("set to DEBUG");
        } else if (level == INT_INFO) {
            setLevel(INFO);
            info("set to INFO");
        } else if (level == INT_WARN) {
            setLevel(WARN);
            warn("set to WARN");
        } else if (level == INT_ERROR) {
            setLevel(ERROR);
            error("set to ERROR");
        } else if (level == INT_FATAL) {
            setLevel(FATAL);
            fatal("set to FATAL");
        } else {
            setLevel(INFO);
        }
    }

    /**
	 * isDebugEnabled() Controle si la priorite DEBUG est activee sur le logger
	 * Permet d'optimiser les performances en evitant que le programme ne doive
	 * construire le message informatif a envoyer au logger si la priorite
	 * requise n'est pas atteinte
	 * 
	 * @return true si le logger est de priorite DEBUG
	 */
    public boolean isDebugEnabled() {
        return _getLogger().isDebugEnabled();
    }

    /**
	 * debug(message) Genere un message de priorite DEBUG
	 */
    public void debug(Object message) {
        _getLogger().debug(message);
    }

    /**
	 * debug(message,t) Genere un message et un throwable de priorite DEBUG
	 */
    public void debug(Object message, Throwable t) {
        _getLogger().debug(message, t);
    }

    /**
	 * Check to see if the INFO priority is enabled for this category.
	 * 
	 * @return true if a {@link #info(Object)} method invocation would pass the
	 *         msg to the configured appenders, false otherwise.
	 */
    public boolean isInfoEnabled() {
        return _getLogger().isInfoEnabled();
    }

    /**
	 * Issue a log msg with a priority of INFO. Invokes log.log(Priority.INFO,
	 * message);
	 */
    public void info(Object message) {
        _getLogger().info(message);
    }

    /**
	 * Issue a log msg and throwable with a priority of INFO. Invokes
	 * log.log(Priority.INFO, message, t);
	 */
    public void info(Object message, Throwable t) {
        _getLogger().info(message, t);
    }

    /**
	 * Issue a log msg with a priority of WARN. Invokes log.log(Priority.WARN,
	 * message);
	 */
    public void warn(Object message) {
        _getLogger().warn(message);
    }

    /**
	 * Issue a log msg and throwable with a priority of WARN. Invokes
	 * log.log(Priority.WARN, message, t);
	 */
    public void warn(Object message, Throwable t) {
        _getLogger().warn(message, t);
    }

    /**
	 * Issue a log msg with a priority of ERROR. Invokes log.log(Priority.ERROR,
	 * message);
	 */
    public void error(Object message) {
        _getLogger().error(message);
    }

    /**
	 * Issue a log msg and throwable with a priority of ERROR. Invokes
	 * log.log(Priority.ERROR, message, t);
	 */
    public void error(Object message, Throwable t) {
        _getLogger().error(message, t);
    }

    /**
	 * Issue a log msg with a priority of FATAL. Invokes log.log(Priority.FATAL,
	 * message);
	 */
    public void fatal(Object message) {
        _getLogger().fatal(message);
    }

    /**
	 * Issue a log msg and throwable with a priority of FATAL. Invokes
	 * log.log(Priority.FATAL, message, t);
	 */
    public void fatal(Object message, Throwable t) {
        _getLogger().fatal(message, t);
    }

    /**
	 * Issue a log msg with the given priority. Invokes log.log(p, message);
	 */
    public void log(Level p, Object message) {
        _getLogger().log(p, message);
    }

    /**
	 * Issue a log msg with the given priority. Invokes log.log(p, message, t);
	 */
    public void log(Level p, Object message, Throwable t) {
        _getLogger().log(p, message, t);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        log = null;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        log = _getLogger();
    }

    /**
	 * Retrieve handle to the root Logger instance
	 */
    public static Syslog getRootLogger() {
        Syslog logger = new Syslog();
        return logger;
    }

    /**
	 * Create a Logger instance given the category name.
	 * 
	 * @param name
	 *            the category name
	 */
    public static Syslog getLogger(String name) {
        Syslog logger = new Syslog(name);
        return logger;
    }

    /**
	 * Create a Logger instance given the category name with the given suffix.
	 * <p>
	 * This will include a category seperator between classname and suffix
	 * 
	 * @param name
	 *            The category name
	 * @param suffix
	 *            A suffix to append to the classname.
	 */
    public static Syslog getLogger(String name, String suffix) {
        return new Syslog(name + "." + suffix);
    }

    /**
	 * Create a Logger instance given the category class. This simply calls
	 * create(clazz.getName()).
	 * 
	 * @param clazz
	 *            the Class whose name will be used as the category name
	 */
    public static Syslog getLogger(Class<?> clazz) {
        Syslog logger = new Syslog(clazz.getName());
        return logger;
    }

    /**
	 * Create a Logger instance given the category class with the given suffix.
	 * <p>
	 * This will include a category seperator between classname and suffix
	 * 
	 * @param clazz
	 *            The Class whose name will be used as the category name.
	 * @param suffix
	 *            A suffix to append to the classname.
	 */
    public static Syslog getLogger(Class<?> clazz, String suffix) {
        return new Syslog(clazz.getName() + "." + suffix);
    }

    public static ListenerAppender getListenerAppender() {
        ListenerAppender appender = (ListenerAppender) Syslog.getRootLogger().getLoggerImpl().getAppender(LISTENER_APPANDER);
        if (appender == null) {
            appender = new ListenerAppender(true);
            appender.setName(LISTENER_APPANDER);
            appender.setLayout(new PatternLayout("%d{HH:mm:ss} %6p [%c{1}] %m%n"));
            Syslog.getRootLogger().getLoggerImpl().addAppender(appender);
        }
        return appender;
    }

    public static void removeListenerAppender() {
        Syslog.getRootLogger().getLoggerImpl().removeAppender(LISTENER_APPANDER);
    }

    public Level getLevel() {
        return _getLogger().getLevel();
    }

    public static Integer getLevelInt() {
        return getRootLogger().getLevel().toInt();
    }
}
