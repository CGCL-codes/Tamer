package ca.uhn.hunit.util.log;

/**
 * Listener which elects to be notified when any logging events are fired by the application
 */
public interface ILogListener {

    void logEvent(LogEvent theLogEvent);
}
