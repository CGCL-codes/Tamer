package net.sf.hiberlog;

import java.util.Date;
import java.util.Vector;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Log4J appender that uses Hibernate to store log events in a database. To use
 * this appender, you must provide two properties in the Log4J properties file:
 * 
 * <UL>
 * sessionServiceClass
 * </UL>
 * <UL>
 * loggingEventClass
 * </UL>
 * <P>
 * The sessionServiceClass must implement the
 * {@link HibernateSessionService} interface which provides the appender
 * with an open Hibernate session. Your implementation of this interface can
 * perform any additional activities such as registering audit interceptors if
 * required.
 * </P>
 * <P>
 * The loggingEventClass must implement the
 * {@link HibernateLoggingEvent} interface. Using an interface for the
 * logging event leaves your implementation free to extend any existing
 * persistence ancestor that you may already be using.
 * </P>
 * <P>
 * An example Log4J properties file to configure the HibernateAppender would be:
 * </P>
 * <code>
 * ### direct messages to Hibernate<BR/>
 * log4j.appender.hibernate=HibernateAppender<BR/>
 * log4j.appender.hibernate.sessionServiceClass=HibernateHelper<BR/>
 * log4j.appender.hibernate.loggingEventClass=LogEvent<BR/>
 * </code>
 * <P>
 * You can now write a Hibernate mapping file for the class specified as the
 * <code>loggingEventClass</code> to persist whichever parts of the logging
 * event that you require.
 * </P>
 * 
 * @author David Howe
 * @version 1.1
 */
public class HibernateAppender extends AppenderSkeleton implements Appender {

    private String sessionServiceClass;

    private String loggingEventClass;

    private HibernateSessionService hibernateSessionServiceImplementation;

    private Class loggingEventWrapperImplementationClass;

    private static Vector buffer = new Vector();

    private static Boolean appending = Boolean.FALSE;

    protected void append(LoggingEvent loggingEvent) {
        synchronized (buffer) {
            buffer.add(loggingEvent);
            synchronized (appending) {
                if (!appending.booleanValue()) {
                    appending = Boolean.TRUE;
                } else {
                    return;
                }
            }
        }
        Transaction tx = null;
        try {
            Session session = hibernateSessionServiceImplementation.openSession();
            synchronized (buffer) {
                tx = session.beginTransaction();
                LoggingEvent bufferLoggingEvent;
                HibernateLoggingEvent loggingEventWrapper;
                int bufferLength = buffer.size();
                for (int i = 0; i < bufferLength; i++) {
                    bufferLoggingEvent = (LoggingEvent) buffer.get(i);
                    try {
                        loggingEventWrapper = (HibernateLoggingEvent) loggingEventWrapperImplementationClass.newInstance();
                    } catch (IllegalAccessException iae) {
                        this.errorHandler.error("Unable to instantiate class " + loggingEventWrapperImplementationClass.getName(), iae, ErrorCode.GENERIC_FAILURE);
                        return;
                    } catch (InstantiationException ie) {
                        this.errorHandler.error("Unable to instantiate class " + loggingEventWrapperImplementationClass.getName(), ie, ErrorCode.GENERIC_FAILURE);
                        return;
                    }
                    loggingEventWrapper.setMessage(bufferLoggingEvent.getRenderedMessage());
                    loggingEventWrapper.setClassName(bufferLoggingEvent.getLocationInformation().getClassName());
                    loggingEventWrapper.setFileName(bufferLoggingEvent.getLocationInformation().getFileName());
                    loggingEventWrapper.setLineNumber(bufferLoggingEvent.getLocationInformation().getLineNumber());
                    Date logDate = new Date();
                    logDate.setTime(bufferLoggingEvent.timeStamp);
                    loggingEventWrapper.setLogDate(logDate);
                    loggingEventWrapper.setLoggerName(bufferLoggingEvent.getLoggerName());
                    loggingEventWrapper.setMethodName(bufferLoggingEvent.getLocationInformation().getMethodName());
                    Date startDate = new Date();
                    startDate.setTime(LoggingEvent.getStartTime());
                    loggingEventWrapper.setStartDate(startDate);
                    loggingEventWrapper.setThreadName(bufferLoggingEvent.getThreadName());
                    if (bufferLoggingEvent.getThrowableStrRep() != null) {
                        for (int j = 0; j < bufferLoggingEvent.getThrowableStrRep().length; j++) {
                            loggingEventWrapper.addThrowableMessage(j, bufferLoggingEvent.getThrowableStrRep()[j]);
                        }
                    }
                    if (bufferLoggingEvent.equals(Level.ALL)) {
                        loggingEventWrapper.setLevel("ALL");
                    } else if (bufferLoggingEvent.getLevel().equals(Level.DEBUG)) {
                        loggingEventWrapper.setLevel("DEBUG");
                    } else if (bufferLoggingEvent.getLevel().equals(Level.ERROR)) {
                        loggingEventWrapper.setLevel("ERROR");
                    } else if (bufferLoggingEvent.getLevel().equals(Level.FATAL)) {
                        loggingEventWrapper.setLevel("FATAL");
                    } else if (bufferLoggingEvent.getLevel().equals(Level.INFO)) {
                        loggingEventWrapper.setLevel("INFO");
                    } else if (bufferLoggingEvent.getLevel().equals(Level.OFF)) {
                        loggingEventWrapper.setLevel("OFF");
                    } else if (bufferLoggingEvent.getLevel().equals(Level.WARN)) {
                        loggingEventWrapper.setLevel("WARN");
                    } else {
                        loggingEventWrapper.setLevel("UNKNOWN");
                    }
                    session.save(loggingEventWrapper);
                }
                tx.commit();
                session.flush();
                buffer.clear();
                synchronized (appending) {
                    appending = Boolean.FALSE;
                }
            }
        } catch (HibernateException he) {
            try {
                tx.rollback();
            } catch (Exception ee) {
            }
            this.errorHandler.error("HibernateException", he, ErrorCode.GENERIC_FAILURE);
            appending = Boolean.FALSE;
            return;
        }
    }

    public void close() {
    }

    public boolean requiresLayout() {
        return false;
    }

    /**
     * Returns the name of class implementing the
     * {@link HibernateSessionService} interface.
     * 
     * @return Fully qualified class name
     */
    public String getSessionServiceClass() {
        return sessionServiceClass;
    }

    /**
     * Sets the name of the class implementing the
     * {@link HibernateSessionService} interface.
     * 
     * @param Fully qualified class name
     */
    public void setSessionServiceClass(String string) {
        this.sessionServiceClass = string;
        try {
            Class c = Class.forName(string);
            try {
                hibernateSessionServiceImplementation = (HibernateSessionService) c.newInstance();
            } catch (InstantiationException ie) {
                this.errorHandler.error("Unable to instantiate class " + c.getName(), ie, ErrorCode.GENERIC_FAILURE);
            } catch (IllegalAccessException iae) {
                this.errorHandler.error("Unable to instantiate class " + c.getName(), iae, ErrorCode.GENERIC_FAILURE);
            }
        } catch (ClassNotFoundException cnfe) {
            this.errorHandler.error("Invalid HibernateAppenderSessionService class " + string, cnfe, ErrorCode.GENERIC_FAILURE);
        }
    }

    /**
     * Returns the name of the class implementing the
     * {@link HibernateLoggingEvent} interface.
     * 
     * @return Fully qualified class name
     */
    public String getLoggingEventClass() {
        return loggingEventClass;
    }

    /**
     * Sets the name of class implementing the
     * {@link HibernateLoggingEvent} interface.
     * 
     * @param Fully qualified class name
     */
    public void setLoggingEventClass(String string) {
        loggingEventClass = string;
        try {
            loggingEventWrapperImplementationClass = Class.forName(loggingEventClass);
        } catch (ClassNotFoundException cnfe) {
            this.errorHandler.error("Invalid LoggingEvent class " + string, cnfe, ErrorCode.GENERIC_FAILURE);
        }
    }
}
