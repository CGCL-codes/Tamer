package shu.cms.measure.cp.msg;

import java.util.logging.*;
import shu.util.log.*;
import shu.util.log.frame.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MessageFrameHandler extends Handler {

    public MessageFrameHandler(LoggerInterface li) {
        super();
        this.setFormatter(new SimplerFormatter());
        this.li = li;
    }

    private LoggerInterface li;

    /**
   * Close the <tt>Handler</tt> and free all associated resources.
   *
   * @throws SecurityException if a security manager exists and if the caller
   *   does not have <tt>LoggingPermission("control")</tt>.
   */
    public void close() throws SecurityException {
    }

    /**
   * Flush any buffered output.
   *
   */
    public void flush() {
    }

    /**
   * Publish a <tt>LogRecord</tt>.
   *
   * @param record description of the log event. A null record is silently
   *   ignored and is not published
   */
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        try {
            String msg = record.getMessage();
            li.log(msg + '\n');
        } catch (Exception ex) {
            reportError(null, ex, ErrorManager.FORMAT_FAILURE);
            return;
        }
    }
}
