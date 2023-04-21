package net.sf.karatasi.desktop;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class GuiLogHandler extends Handler {

    /** The LogPanel at which the logging shall be displayed. */
    private final GuiLogPanel logPanel;

    /** Creates a GuiLog.
     * @param logPanel The logPanel at which the logging shall occur.
     */
    public GuiLogHandler(final GuiLogPanel logPanel) {
        this.logPanel = logPanel;
    }

    /** {@inheritDoc} */
    @Override
    public void publish(final LogRecord record) {
        logPanel.log(record);
    }

    /** {@inheritDoc} */
    @Override
    public void flush() {
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws SecurityException {
    }
}
