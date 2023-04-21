package jir.common;

import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * This singleton class controls output to the textarea on the bottom of the screen.
 * Functions as a replacement for the system console.
 * @author Wesley van Beelen, Mathijs Lagerberg, Paul Lammertsma, Jeff Ouwerkerk, Erik van der Pol
 */
public class Logger {

    private ArrayList<JTextArea> listeners;

    private JLabel label;

    private JProgressBar progress;

    private boolean on;

    private boolean addToLabel;

    /**
	 * Singleton Logger object.
	 */
    public static Logger out;

    static {
        out = new Logger();
    }

    private Logger() {
        listeners = new ArrayList<JTextArea>();
        on = true;
        addToLabel = false;
    }

    /**
	 * Adds a textarea as a 'Log Listener'. All data sent to the Logger will appear in the textarea.
	 * @param t the textarea to add to the list of listeners.
	 */
    public void addLogListener(JTextArea t) {
        listeners.add(t);
    }

    /**
     * Sets a JLabel as a display for progress information.
     * Only one label can be added at a time (calling this function twice will replace the previous label).
     * @param l the label that will output the status message outputted using {@link #print} or {@link #println}.
     */
    public void setLabelListener(JLabel l) {
        this.label = l;
    }

    /**
	 * Sets a JProgressBar as a listener for progress information.
	 * Only one progressbar can be added at a time (calling this function twice will replace the previous listener).
	 * @param p the progressbar that will output the status set by {@link #updateProgress}.
	 */
    public void setProgressListener(JProgressBar p) {
        progress = p;
        progress.setIndeterminate(false);
        progress.setMinimum(0);
        progress.setMaximum(100);
    }

    /**
	 * Clears the contents of all listening textareas.
	 */
    public void clear() {
        setLabelText("");
        addToLabel = false;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (on) {
                    for (JTextArea t : listeners) {
                        String text = t.getText();
                        text = text.substring(text.lastIndexOf("\n", text.length() - 2) + 1);
                        t.setText("");
                        print(text);
                        t.setCaretPosition(t.getDocument().getLength());
                        t.repaint();
                    }
                }
            }
        });
    }

    /**
	 * Appends a string to all listening textareas.
	 * @param s the string to append to the content of the textareas.
	 */
    public void print(final String s) {
        setLabelText(s);
        addToLabel = true;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (on) {
                    for (JTextArea t : listeners) {
                        t.append(s);
                        t.setCaretPosition(t.getDocument().getLength());
                        t.repaint();
                    }
                }
            }
        });
    }

    /**
     * Appends a string to all listening textareas, with a trailing newline character.
     * @param s the string to append to the content of the textareas.
     */
    public void println(String s) {
        this.print(s + "\n");
        addToLabel = false;
    }

    /**
     * Appends a string to all listening textareas.
     * Calls the <code>toString</code> method of the given object to retreive the string.
     * @param o the object to append to the content of the textareas.
     */
    public void println(Object o) {
        this.println(o.toString());
    }

    /**
	 * Appends a newline character to the content of all listening textareas.
	 */
    public void println() {
        this.print("\n");
        addToLabel = false;
    }

    /**
	 * Updates the listening progressbar to display the given percentage.
	 * @param p the percentage to display (must be in range 0 to 100).
	 */
    public void updateProgress(final int p) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progress.setValue(p);
                progress.repaint();
            }
        });
    }

    /**
	 * Switches visibility of the listening progressbar, and resets the progress to 0%.
	 * @param v when <code>true</code>, the progressbar will show the current status.
	 */
    public void progressSetVisible(final boolean v) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (v) {
                    updateProgress(0);
                    progress.setStringPainted(true);
                } else {
                    progress.setStringPainted(false);
                    updateProgress(0);
                }
            }
        });
    }

    /**
	 * Switches the listening progressbar to either indeterminate or determinate mode.
	 * @param i when <code>true</code>, the progressbar will show the status as a percentage from 0 to 100%, when <code>false</code>,
	 *          the length of the current task is unknown.
	 */
    public void progressIndeterminate(final boolean i) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progress.setIndeterminate(i);
            }
        });
    }

    /**
	 * Switches the display of output messages off.
	 */
    public void turnOff() {
        this.on = false;
    }

    /**
     * Switches the display of output messages on.
     */
    public void turnOn() {
        this.on = true;
    }

    private void setLabelText(String s) {
        if (label == null) return;
        if (addToLabel) {
            label.setText(label.getText() + s);
        } else {
            label.setText(s);
        }
    }

    /**
	 * Set text on label to "Ready."
	 */
    public void setLabelDone() {
        if (label == null) return;
        label.setText("Ready.");
        addToLabel = false;
    }
}
