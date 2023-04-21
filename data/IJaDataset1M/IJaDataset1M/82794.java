package simtools.logging;

public interface LoggingEntry {

    /**
     * @return the event level
     */
    public int getLevel();

    /**
     * @return event time in milliseconds since 1970
     */
    public long getMillis();

    /**
     * @return the sequence number
     */
    public long getSequenceNumber();

    /**
     * @return class that issued logging call
     */
    public String getSourceClassName();

    /**
     * @return method that issued logging call
     */
    public String getSourceMethodName();

    /**
     * @return name of the source Logger.
     */
    public String getLoggerName();

    /**
     * @return the message
     */
    public String getMessage();

    /**
     * @return thread ID for thread that issued logging call.
     */
    public int getThreadID();
}
