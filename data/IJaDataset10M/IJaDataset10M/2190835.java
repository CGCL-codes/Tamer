package rabbit.proxy;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/** A writer for log files.
 */
class LogWriter extends PrintWriter {

    private OutputStream os;

    public LogWriter(OutputStream os, boolean autoFlush) {
        super(os, autoFlush);
        this.os = os;
    }

    public LogWriter(Writer w, boolean autoFlush) {
        super(w, autoFlush);
        os = null;
    }

    public boolean isSystemWriter() {
        return (os == System.out || os == System.err);
    }
}
