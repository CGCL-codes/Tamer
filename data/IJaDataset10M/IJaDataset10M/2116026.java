package diuf.diva.hephaistk.config;

import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import diuf.diva.hephaistk.undercity.agents.LogWatcherGui;

public class HephaistkLogHandler extends Handler {

    private Vector<LogRecord> records = null;

    private LogWatcherGui client = null;

    public HephaistkLogHandler() {
        records = new Vector<LogRecord>();
    }

    @Override
    public void close() throws SecurityException {
    }

    @Override
    public void flush() {
        records.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void publish(LogRecord record) {
        records.add(record);
        if (client != null && client.isReadyToReceiveLogData()) {
            client.sendLogToDisplay((Vector<LogRecord>) records.clone());
            records.clear();
        }
    }

    public void registerClient(LogWatcherGui client) {
        this.client = client;
    }
}
