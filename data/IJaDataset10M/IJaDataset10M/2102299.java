package org.rakiura.rak;

/**
 * The RAK proxy class for logging facility. This particular
 * implementation is using JDK 1.4 logging API.  When needed,
 * additional methods should be written here (currently partial
 * compatibility with logging API is provided.) 
 * 
 *<br><br>
 * Logger.java<br>
 * Created: Thu Nov  8 22:44:35 2001<br>
 *
 * @author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @version $Revision: 1.6 $ $Date: 2002/02/25 23:26:30 $
 */
public abstract class Logger {

    public static Logger getLogger(String name) {
        if (JavaVersion.getJavaVersion() == JavaVersion.JAVA_1_4) return JDK14Logger.getLogger(name);
        return SimpleStringLogger.getLogger(name);
    }

    public static Logger getLogger(String name, String resources) {
        if (JavaVersion.getJavaVersion() == JavaVersion.JAVA_1_4) return JDK14Logger.getLogger(name, resources);
        return SimpleStringLogger.getLogger(name, resources);
    }

    public abstract void setup();

    /** Log a CONFIG message.*/
    public abstract void config(String msg);

    /**  Log a FINE message. */
    public abstract void fine(String msg);

    /**  Log a FINER message. */
    public abstract void finer(String msg);

    /** Log an INFO message. */
    public abstract void info(String msg);

    /** Log a SEVERE message. */
    public abstract void severe(String msg);

    /** Log a WARNING message. */
    public abstract void warning(String msg);
}
