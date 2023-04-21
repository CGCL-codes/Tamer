package org.freehold.jukebox.logger;

public interface Logger {

    /**
     * Accept a log message.
     *
     * @param originator The object on whose behalf the message is logged.
     *
     * @param ll The severity level.
     *
     * @param channel The channel the message belongs to.
     *
     * @param message The message itself.
     *
     * @param t The exception logged along with the message.
     */
    public void complain(Object originator, LogLevel ll, LogChannel channel, Object message, Throwable t);

    /**
     * Get this logger's channel.
     *
     * @return The logger channel.
     */
    public LogChannel getChannel();

    /**
     * Attach the logger to the log target.
     *
     * The logger will be useless unless attached to the log target.
     */
    public void attach(LogTarget target);

    /**
     * Flush the buffer, if any.
     */
    public void flush();

    /**
     * Close the logger.
     *
     * It is a responsibility of the implementor to flush all the buffers,
     * if any.
     */
    public void close();
}
