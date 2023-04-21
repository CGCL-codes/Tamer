package freets.gui.design;

import freets.gui.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A status bar GUI component.
 * 
 * @author T. F�rster
 * @version $Revision: 1.1.1.1 $
 */
public final class StatusBar extends JPanel implements Runnable {

    private static final int TIMEOUT = 30;

    protected JLabel textLabel;

    protected String defaultText;

    protected int countDown;

    protected Thread thread;

    private JLabel dummy1, dummy2;

    /**
     * Create a new StatusBar with an empty text.
     */
    public StatusBar() {
        super(new BorderLayout());
        textLabel = new JLabel(" ");
        setBorder(BorderFactory.createLineBorder(Color.black));
        add(textLabel, "Center");
        dummy1 = new JLabel();
        dummy2 = new JLabel();
        dummy1.setPreferredSize(new Dimension(4, 4));
        dummy2.setPreferredSize(new Dimension(4, 4));
        add(dummy1, "North");
        add(dummy2, "West");
        countDown = TIMEOUT;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Sets the default string that is displayed  a short period of time (30 secs) after
     * the text has been changed.
     * @param t the default text 
     */
    public void setDefaultText(String t) {
        defaultText = t;
    }

    /**
     * Sets the text to be displayed.
     * @param t the text.
     */
    public void setText(String t) {
        textLabel.setText(t);
        countDown = TIMEOUT;
    }

    /**
     * Display the default-text.
     */
    public void displayDefaultText() {
        textLabel.setText(defaultText);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException iex) {
            } finally {
                countDown--;
                if (countDown == 0) {
                    countDown = TIMEOUT;
                    setText(defaultText);
                }
            }
        }
    }
}
