package net.sf.webwarp.modules.eventlog;

import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

/**
 * The event log provider is the DAO component for saving and retrieving of log entries. It is used by the EventLogger
 * instances when logging to the database.
 * 
 * @author atr
 * @see EventLoggerImpl
 * @param <T>
 */
public interface EventLogProvider<T extends EventLog> {

    /**
     * Creates a log entry.
     * 
     * @param clazz
     *            The base entity class
     * @param src
     *            The source ID
     * @param runID
     *            The (optional) run ID
     * @param logLevel
     *            The log level
     * @param message
     *            The log message
     * @param t
     *            The (optional) exception to be logged
     * @param data
     *            The (optional) values for additional data attached
     * @return The log entry's key
     */
    Long log(Class<T> clazz, String src, String runID, LogLevel logLevel, String message, Throwable t, Map<String, Object> data);

    /**
     * Retrieve all logs that match the given criteria.
     * 
     * @param clazz
     *            The base class
     * @param logLevel
     *            The log level
     * @param src
     *            The source ID
     * @param runID
     *            The run ID
     * @param message
     *            The log message
     * @param fromDate
     *            Entries after the given date
     * @param toDate
     *            Entries before the given date
     * @return The list of LogEntry instances found
     */
    List<T> getLogs(Class<T> clazz, LogLevel logLevel, String src, String runID, String message, DateTime fromDate, DateTime toDate);

    /**
     * Retrieve all logs that match the given criteria.
     * 
     * @param clazz
     *            The base class
     * @param logLevel
     *            The log level
     * @param srcIDs
     *            The source IDs
     * @param runID
     *            The run ID
     * @param message
     *            The log message
     * @param fromDate
     *            Entries after the given date
     * @param toDate
     *            Entries before the given date
     * @return The list of LogEntry instances found
     */
    List<T> getLogs(Class<T> clazz, LogLevel logLevel, List<String> srcIDs, String runID, String message, DateTime fromDate, DateTime toDate);

    /**
     * Gets a log entry by its ID.
     * 
     * @param id
     *            The entry's ID
     * @return The log entry found or NULL.
     */
    EventLog loadLog(Long id);

    /**
     * Retrieves all available source IDs that logged during a certain job (given its runID).
     * 
     * @param runID
     *            The job run ID
     * @return The different source IDs of the matching entries.
     */
    List<String> getLogSources(String runID);

    /**
     * Delete all log entries of a certain log level that are older than the given date.
     * 
     * @param level
     *            The log level
     * @param dateUntil
     *            The daten until all matching entries are deleted
     */
    void delete(LogLevel level, DateTime dateUntil);

    /**
     * Deletes all log entries that correspond to a certain job runID.
     * 
     * @param runID
     *            The matching runID
     */
    void delete(String runID);

    /**
     * Deletes all log entries that correspond to a certain log level.
     * 
     * @param logLevel
     *            The matching log level
     */
    void delete(LogLevel logLevel);
}
