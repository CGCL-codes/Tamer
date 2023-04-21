package ch.tarnet.library.logger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class TwinHandler extends Handler {

    @Override
    public void close() throws SecurityException {
        flush();
    }

    @Override
    public void flush() {
        System.out.flush();
        System.err.flush();
    }

    @Override
    public void publish(LogRecord record) {
        String rec = getFormatter().format(record);
        if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
            System.err.print(rec);
        } else {
            System.out.print(rec);
        }
    }
}
