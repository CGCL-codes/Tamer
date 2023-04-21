package bufferings.ktr.wjr.server.logic;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * The recorder of the GAE Development Server log.
 * 
 * This class records the log, cpu time and api cpu time.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrGAEDevLogRecorder extends WjrGAELogRecorder {

    /**
   * The name of the root logger.
   */
    protected static final String ROOT_LOGGER_NAME = "";

    /**
   * The log handler.
   */
    protected static DevServerLogHandler devServerLogHandler;

    /**
   * {@inheritDoc}
   */
    @Override
    protected void startBare() {
        if (devServerLogHandler == null) {
            devServerLogHandler = new DevServerLogHandler();
            Logger.getLogger(ROOT_LOGGER_NAME).addHandler(devServerLogHandler);
        }
        devServerLogHandler.recorder = this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    protected void stopBare() {
        devServerLogHandler.recorder = null;
    }

    /**
   * Formats the log.
   * 
   * @param logRecord
   *          The log record.
   * @return The formatted string.
   */
    protected String formatLog(LogRecord record) {
        String message = record.getSourceClassName() + " " + record.getSourceMethodName() + ": " + record.getMessage();
        return formatLog(record.getMillis(), record.getLevel().getName(), message);
    }

    /**
   * The log handler for the development server log recording.
   * 
   * @author bufferings[at]gmail.com
   */
    protected static class DevServerLogHandler extends Handler {

        /**
     * The recorder of the GAE Development Server log.
     */
        public WjrGAEDevLogRecorder recorder;

        /**
     * {@inheritDoc}
     */
        public void close() throws SecurityException {
        }

        /**
     * {@inheritDoc}
     */
        public void flush() {
        }

        /**
     * {@inheritDoc}
     */
        public void publish(LogRecord record) {
            if (recorder != null) {
                recorder.log.append(recorder.formatLog(record));
            }
        }
    }
}
