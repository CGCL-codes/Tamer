package net.sf.statcvs.util;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * A simplified copy of <code>java.util.logging.ConsoleHandler</code>.
 * It writes to <code>System.out</code> instead of
 * <code>System.err</code> and uses the {@link LogFormatter}
 * to format 
 * @author Richard Cyganiak <rcyg@gmx.de>
 * @version $Id: ConsoleOutHandler.java,v 1.3 2008/04/02 11:22:15 benoitx Exp $
 */
public class ConsoleOutHandler extends StreamHandler {

    /**
     * Create a <tt>ConsoleOutHandler</tt> for <tt>System.out</tt>.
     */
    public ConsoleOutHandler() {
        setLevel(Level.FINEST);
        setFormatter(new LogFormatter());
        setOutputStream(System.out);
    }

    /**
     * Publish a <tt>LogRecord</tt>.
     * <p>
     * The logging request was made initially to a <tt>Logger</tt> object,
     * which initialized the <tt>LogRecord</tt> and forwarded it here.
     * <p>
     * @param  record  description of the log event
     */
    public void publish(final LogRecord record) {
        super.publish(record);
        flush();
    }

    /**
     * Override <tt>StreamHandler.close</tt> to do a flush but not
     * to close the output stream.  That is, we do <b>not</b>
     * close <tt>System.err</tt>.
     */
    public void close() {
        flush();
    }
}
