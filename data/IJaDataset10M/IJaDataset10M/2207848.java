package cx.ath.contribs.util.io;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LMRootLogger extends LMLogger {

    protected LMRootLogger() {
        super("", null);
        setLevel(Level.INFO);
    }

    public void log(LogRecord record) {
        _manager.initializeGlobalHandlers();
        super.log(record);
    }

    public void addHandler(Handler h) {
        _manager.initializeGlobalHandlers();
        super.addHandler(h);
    }

    public void removeHandler(Handler h) {
        _manager.initializeGlobalHandlers();
        super.removeHandler(h);
    }

    public Handler[] getHandlers() {
        _manager.initializeGlobalHandlers();
        return super.getHandlers();
    }
}
