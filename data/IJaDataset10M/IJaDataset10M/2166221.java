package fr.macymed.commons.logging.log4j;

import org.apache.log4j.Level;

/** 
 * <p> 
 * This class contains default level for the Macymed Log4j Logging Library.
 * </p>
 * @author <a href="mailto:alexandre.cartapanis@macymed.fr">Cartapanis Alexandre</a>
 * @version 1.0.5
 * @since Commons - Log4j Logging API 2.0
 */
public class Levels {

    /** ALL indicates that all messages should be logged. This level is initialized to <CODE>Integer.MIN_VALUE</CODE>. */
    public static final Level ALL = Level.ALL;

    /** DEBUG is a message level providing tracing information. In general the DEBUG level should be used for information that will be broadly interesting to developers who do not have a specialized interest in the specific subsystem. DEBUG messages might include things like minor (recoverable) failures. Issues indicating potential performance problems are also worth logging as DEBUG. */
    public static final Level DEBUG = Level.DEBUG;

    /** CONFIG is a message level for static configuration messages. CONFIG messages are intended to provide a variety of static configuration information, to assist in debugging problems that may be associated with particular configurations. For example, CONFIG message might include the CPU type, the graphics depth, the GUI look-and-feel, etc. */
    public static final Level CONFIG = Level.DEBUG;

    /** INFO is a message level for informational messages. Typically INFO messages will be written to the console or its equivalent. So the INFO level should only be used for reasonably significant messages that will make sense to end users and system admins. */
    public static final Level INFO = Level.INFO;

    /** WARNING is a message level indicating a potential problem. In general WARNING messages should describe events that will be of interest to end users or system managers, or which indicate potential problems. */
    public static final Level WARNING = Level.WARN;

    /** ERROR is a message level indicating a serious failure. In general ERROR messages should describe events that are of considerable importance and which will prevent normal program execution. */
    public static final Level ERROR = Level.ERROR;

    /** FATAL is a message level indicating a major failure. In general FATAL messages should describe events that are responsible of program stops. */
    public static final Level FATAL = Level.FATAL;

    /** OFF is a special level that can be used to turn off logging. */
    public static final Level OFF = Level.OFF;

    /** The default level. */
    public static final Level DEFAULT = INFO;
}
