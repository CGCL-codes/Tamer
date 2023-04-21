package de.teamwork.log;

import de.teamwork.util.Store;
import java.awt.*;
import java.awt.event.*;

/**
 * A simple <code>LogWindow</code> implementation. Contains a command history.
 * Great.
 *
 * @author Christoph Daniel Schulze
 * @version Beta 2.0
 */
public class LogAWTWindow extends Frame implements LogWindow, ActionListener {

    /**
     * The parent <code>Log</code> object.
     */
    private Log parentLog = null;

    private ScrollPane logScrollPane;

    private TextArea logTextArea;

    private Panel bottomPanel;

    private TextField commandField;

    /**
     * Creates a new <code>LogSwingWindow</code>.
     */
    public LogAWTWindow() {
        super();
        createUI();
    }

    /**
     * Initializes the GUI stuff.
     */
    protected void createUI() {
        logTextArea = new TextArea("");
        logTextArea.setEditable(false);
        logTextArea.setFont(new Font("Monospaced", 0, 12));
        logScrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        logScrollPane.add(logTextArea);
        commandField = new TextField();
        commandField.setEditable(true);
        commandField.setFont(new Font("Monospaced", 0, 12));
        commandField.setName("field");
        commandField.setVisible(false);
        commandField.addActionListener(this);
        add(logScrollPane, BorderLayout.CENTER);
        add(commandField, BorderLayout.SOUTH);
        setSize(480, 320);
    }

    /**
     * Returns if this <code>LogWindow</code> acts as a console or not.
     *
     * @return Boolean value indicating if this <code>LogWindow</code> acts as a
     *        console (<code>true</code>) or not (<code>false</code>).
     */
    public boolean getConsoleMode() {
        return commandField.isVisible();
    }

    /**
     * Sepcifies, if this <code>LogWindow</code> can also act as a console or
     * not.
     *
     * @param mode <code>true</code> if it should act as a console,
     *            <code>false</code> otherwise.
     */
    public void setConsoleMode(boolean mode) {
        commandField.setVisible(mode);
    }

    /**
     * Returns the parent <code>Log</code> for this <code>LogWindow</code>.
     *
     * @return Parent <code>Log</code> object or null if none has been set
     *        (should never be the case).
     */
    public Log getParentLog() {
        return parentLog;
    }

    /**
     * Used by the <code>Log</code> class that uses this <code>LogWindow</code>
     * to set itself as the <code>Log</code> class to call in case of a console
     * command. Shouldn't be used by other classes.
     *
     * @param parent <code>Log</code> object that uses this
     *              <code>LogWindow</code>.
     */
    public void setParentLog(Log parent) {
        parentLog = parent;
    }

    /**
     * Appends the given log message to the log content. Should be synchronized.
     *
     * @param message String object containing the message that is to be
     *               appended. It does not contain any line seperators at its
     *               end, so this has to be managed by the
     *               <code>LogWindow</code> implementation.
     * @param level   The message level. For more information on levels, see the
     *               <code>Log</code> class documentation.
     */
    public synchronized void append(String message, String level) {
        logTextArea.append(message + "\n");
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }

    /**
     * Event-Dispatcher.
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        if (parentLog != null) {
            String temp = (String) commandField.getText();
            if (!temp.equals("")) {
                commandField.setText("");
                parentLog.issueConsoleCommand(temp);
            }
        }
    }
}
