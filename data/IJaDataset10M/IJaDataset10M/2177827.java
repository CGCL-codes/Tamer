package org.neblipedia.gui.swing;

import javax.swing.JTextArea;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author juan
 * 
 */
public class MiAppender extends AppenderSkeleton {

    private JTextArea jtextarea = new JTextArea(10, 10);

    public MiAppender() {
        super();
    }

    @Override
    protected void append(LoggingEvent log) {
        jtextarea.append(log.getMessage() + "\n");
    }

    @Override
    public void close() {
    }

    public JTextArea getJtextarea() {
        return jtextarea;
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
