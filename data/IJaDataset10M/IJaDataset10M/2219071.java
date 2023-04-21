package org.iaccess.gui;

import javax.swing.JTextArea;
import java.util.logging.*;

/**
 *
 * @author Ernesto Jose Perez Garcia
 */
public class TextAreaHandler extends Handler {

    private JTextArea textArea = null;

    /**
   * private constructor, preventing initialization
   */
    public TextAreaHandler(JTextArea textArea) {
        this.textArea = textArea;
        configure();
    }

    /**
   * This method loads the configuration properties from the JDK level
   * configuration file with the help of the LogManager class. It then sets
   * its level, filter and formatter properties.
   */
    private void configure() {
        LogManager manager = LogManager.getLogManager();
        String className = this.getClass().getName();
        String level = manager.getProperty(className + ".level");
        setLevel(level != null ? Level.parse(level) : Level.INFO);
    }

    /**
   * This is the overridden publish method of the abstract super class
   * Handler. This method writes the logging information to the associated
   * Java window. This method is synchronized to make it thread-safe. In case
   * there is a problem, it reports the problem with the ErrorManager, only
   * once and silently ignores the others.
   *
   * @record the LogRecord object
   *
   */
    public synchronized void publish(LogRecord record) {
        String message = null;
        if (!isLoggable(record)) return;
        try {
            message = getFormatter().format(record);
        } catch (Exception e) {
            reportError(null, e, ErrorManager.FORMAT_FAILURE);
        }
        try {
            textArea.append(message);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        } catch (Exception ex) {
            reportError(null, ex, ErrorManager.WRITE_FAILURE);
        }
    }

    public void close() {
    }

    public void flush() {
    }
}
