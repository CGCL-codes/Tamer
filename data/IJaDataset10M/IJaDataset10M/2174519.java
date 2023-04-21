package org.stanwood.media.logging;

import java.io.PrintStream;
import java.text.MessageFormat;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

/**
 * The StreamAppender will log events Streams. Error and Fatal level
 * events will be sent to a error stream, and the other levels to the output
 * stream. By default, the error and output streams are set to use the
 * console streams.
 */
public class StreamAppender extends AppenderSkeleton {

    private PrintStream errorStream = System.err;

    private PrintStream outputStream = System.out;

    private boolean printStackTrace = false;

    /**
	 * Immediate flush means that the underlying writer or output stream will be flushed at the end of each append
	 * operation. Immediate flush is slower but ensures that each append request is actually written. If
	 * <code>immediateFlush</code> is set to <code>false</code>, then there is a good chance that the last few logs
	 * events are not actually written to persistent media if and when the application crashes.
	 *
	 * <p>
	 * The <code>immediateFlush</code> variable is set to <code>true</code> by default.
	 */
    protected boolean immediateFlush = true;

    /**
	 * This default constructor does nothing.
	 */
    public StreamAppender() {
    }

    /**
	 * Creates a configured appender using the console streams.
	 * @param layout The layout to use.
	 */
    public StreamAppender(Layout layout) {
        this(layout, System.out, System.err);
    }

    /**
	 * Creates a configured appender using the given streams.
	 * @param layout The layout to use.
	 * @param outputStream The output stream to use
	 * @param errorStream The error stream to use
	 */
    public StreamAppender(Layout layout, PrintStream outputStream, PrintStream errorStream) {
        setLayout(layout);
        setTargets(errorStream, outputStream);
        activateOptions();
    }

    private void setTargets(PrintStream errorStream, PrintStream outputStream) {
        this.errorStream = errorStream;
        this.outputStream = outputStream;
    }

    /**
	 * If the <b>ImmediateFlush</b> option is set to <code>true</code>, the appender will flush at the end of each
	 * write. This is the default behaviour. If the option is set to <code>false</code>, then the underlying stream can
	 * defer writing to physical medium to a later time.
	 *
	 * <p>
	 * Avoiding the flush operation at the end of each append results in a performance gain of 10 to 20 percent.
	 * However, there is safety tradeoff involved in skipping flushing. Indeed, when flushing is skipped, then it is
	 * likely that the last few log events will not be recorded on disk when the application exits. This is a high price
	 * to pay even for a 20% performance gain.
	 * @param value True to enable immediate flush, otherwise false.
	 */
    public void setImmediateFlush(boolean value) {
        immediateFlush = value;
    }

    /**
	 * Returns value of the <b>ImmediateFlush</b> option.
	 * @return True if immediate flush is enabled, otherwise false.
	 */
    public boolean getImmediateFlush() {
        return immediateFlush;
    }

    /**
	 * Prepares the appender for use.
	 */
    @Override
    public void activateOptions() {
    }

    /**
	 * <p>This method is called by the {@link AppenderSkeleton#doAppend} method.</p>
	 *
	 * <p>
	 * This will write ERROR and FATAL messages to the error stream and the other levels
	 * to the output stream. If it is unable to write to the stream, then a single
	 * warning message is written to <code>System.err</code>.
	 * </p>
	 *
	 * <p>The format of the output will depend on this appender's layout.</p>
	 * @param event The event to log
	 */
    @Override
    public void append(LoggingEvent event) {
        if (!checkEntryConditions()) {
            return;
        }
        subAppend(event);
    }

    /**
	 * This method determines if there is a sense in attempting to append.
	 *
	 * <p>
	 * It checks whether there is a set output target and also if there is a set layout. If these checks fail, then the
	 * boolean value <code>false</code> is returned.
	 * @return true if message should be appended, otherwise false
	 */
    protected boolean checkEntryConditions() {
        if (this.closed) {
            LogLog.warn(Messages.getString("StreamAppender.APPENDER_CLOSED"));
            return false;
        }
        if (this.errorStream == null) {
            errorHandler.error(MessageFormat.format(Messages.getString("StreamAppender.NO_ERROR_STREAM"), name));
            return false;
        }
        if (this.outputStream == null) {
            errorHandler.error(MessageFormat.format(Messages.getString("StreamAppender.NO_OUTPUT_STREAM"), name));
            return false;
        }
        if (this.layout == null) {
            errorHandler.error(MessageFormat.format(Messages.getString("StreamAppender.NO_LAYOUT"), name));
            return false;
        }
        return true;
    }

    /**
	 * Close this appender instance. The underlying stream or writer is also closed.
	 *
	 * <p>
	 * Closed appenders cannot be reused.
	 */
    @Override
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        writeFooter();
        reset();
    }

    /**
	 * Close the underlying streams
	 * */
    protected void closeStreams() {
        if (outputStream != null) {
            outputStream.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }
    }

    /**
	 * Actual writing occurs here.
	 *
	 * <p>
	 * Most subclasses of <code>WriterAppender</code> will need to override this method.
	 * @param event The logging event
	 */
    protected void subAppend(LoggingEvent event) {
        PrintStream stream = getStream(event.getLevel());
        String msg = this.layout.format(event);
        stream.print(msg);
        if (layout.ignoresThrowable()) {
            boolean renderStack = true;
            if (event.getThrowableInformation() != null && event.getThrowableInformation().getThrowable() instanceof StanwoodException) {
                if (event.getLevel().isGreaterOrEqual(Level.DEBUG)) {
                    renderStack = false;
                }
            }
            if (renderStack || this.printStackTrace) {
                String[] s = event.getThrowableStrRep();
                if (s != null) {
                    int len = s.length;
                    for (int i = 0; i < len; i++) {
                        stream.print(s[i]);
                        stream.print(Layout.LINE_SEP);
                    }
                }
            } else {
                stream.print(((StanwoodException) event.getThrowableInformation().getThrowable()).printException(msg));
                stream.print(Layout.LINE_SEP);
            }
        }
        if (this.immediateFlush) {
            stream.flush();
        }
    }

    private PrintStream getStream(Level level) {
        PrintStream stream = null;
        if (level.equals(Level.ERROR) || level.equals(Level.FATAL)) {
            stream = errorStream;
        } else {
            stream = outputStream;
        }
        return stream;
    }

    /**
	 * The WriterAppender requires a layout. Hence, this method returns <code>true</code>.
	 * @return Always returns true.
	 */
    @Override
    public boolean requiresLayout() {
        return true;
    }

    /**
	 * Clear internal references to the writer and other variables.
	 *
	 * Subclasses can override this method for an alternate closing behavior.
	 */
    protected void reset() {
        closeStreams();
        this.outputStream = null;
        this.errorStream = null;
    }

    /**
	 * Write a footer as produced by the embedded layout's {@link Layout#getFooter} method.
	 */
    protected void writeFooter() {
        if (layout != null) {
            String f = layout.getFooter();
            if (f != null && this.outputStream != null) {
                this.outputStream.print(f);
                this.outputStream.flush();
            }
        }
    }

    /**
	 * Write a header as produced by the embedded layout's {@link Layout#getHeader} method.
	 */
    protected void writeHeader() {
        if (layout != null) {
            String h = layout.getHeader();
            if (h != null && this.outputStream != null) {
                this.outputStream.print(h);
            }
        }
    }

    /**
	 * Returns true if stack traces should be printed to the log
	 * @return true if stack traces should be printed to the log
	 */
    public boolean isPrintStackTrace() {
        return printStackTrace;
    }

    /**
	 * If this is set to true, the stack traces will be printed
	 * @param printStackTrace set to true, the stack traces will be printed
	 */
    public void setPrintStackTrace(boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
    }
}
