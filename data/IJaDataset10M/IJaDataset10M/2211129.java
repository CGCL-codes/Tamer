package toolkit.levelEditor.gui;

import java.awt.BorderLayout;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBar extends JPanel {

    private final JLabel label;

    public StatusBar(String logName) {
        if (null == logName) logName = "";
        label = new JLabel();
        setLayout(new BorderLayout());
        add(label);
        Logger.getLogger(logName).addHandler(new Handler() {

            @Override
            public void close() throws SecurityException {
            }

            @Override
            public void flush() {
            }

            @Override
            public void publish(final LogRecord record) {
                label.setText(record.getMessage());
            }
        });
    }
}
