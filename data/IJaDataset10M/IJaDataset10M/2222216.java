package com.anthonyeden.lib.log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import com.anthonyeden.lib.util.ClassUtilities;
import com.anthonyeden.lib.util.MethodUtilities;

/**	Core class for obtaining Loggers.

	<p>If you would like to implement a custom logger you must follow these
	rules:
	
	<ul>
	<li>Must implement LoggerInternal</li>
	<li>Must include two static configuration methods:
		<ul>
		<li><code>public static void configure()</code></li>
		<li><code>public static void configure(File file)</code></li>
		</ul>
	</li>
	</ul>
	
	<p>Loggers will often wrap other logging APIs.  By having a consistant
	interface to log facilities you do not need to embed code from any
	one log facility into your system.
    
    <b>This class is deprecated.</b>  All EdenLib classes now use the
    Apache Jakarta Commons logging library.

	@author Anthony Eden
    @deprecated
*/
public class LogManager {

    /**	Configure the underlying log implementation.
	
		@throws Exception
	*/
    public static final void configure() throws Exception {
        Class loggerClass = ClassUtilities.loadClass(getLoggerClassName(), LogManager.class);
        MethodUtilities.invoke("configure", null, loggerClass, null);
    }

    /**	Configure the underlying log implementation using the given
		file.
		
		@throws Exception
	*/
    public static final void configure(File file) throws Exception {
        Class loggerClass = ClassUtilities.loadClass(getLoggerClassName(), LogManager.class);
        MethodUtilities.invoke("configure", null, loggerClass, file);
    }

    /**	Return the name of the logger implementation class to use.
	
		@return The logger implementation classname
	*/
    public static String getLoggerClassName() {
        return loggerClassName;
    }

    /**	Set the name of the logger implementation class to use.
	
		@param className The new logger implementation classname
	*/
    public static void setLoggerClassName(String className) {
        loggerClassName = className;
    }

    /**	Get a logger for the given object.  This method will use the
		objects class name as the logging identifier.
		
		@param object The object
		@return The logger
	*/
    public static Logger getLogger(Object object) {
        return getLogger(object.getClass().getName());
    }

    /**	Get the logger using the given name.
	
		@param name The name of the logger
		@return The logger
	*/
    public static synchronized Logger getLogger(String name) {
        LoggerInternal logger = (LoggerInternal) loggers.get(name);
        if (logger == null) {
            try {
                logger = (LoggerInternal) ClassUtilities.loadClass(loggerClassName, LogManager.class).newInstance();
            } catch (Exception e) {
                logger = defaultLogger;
            }
            logger.init(name);
            loggers.put(name, logger);
        }
        return logger;
    }

    public static final int DEBUG = 1;

    public static final int INFO = 2;

    public static final int WARN = 3;

    public static final int ERROR = 4;

    public static final int FATAL = 5;

    private static String loggerClassName = SystemErrorLogger.class.getName();

    private static LoggerInternal defaultLogger = new Log4JLogger();

    private static HashMap loggers = new HashMap();
}
