package net.sf.nebulacards.uicomponents;

import java.awt.*;
import java.awt.event.*;
import net.sf.nebulacards.main.*;
import net.sf.nebulacards.uicomponents.*;

/**
 * The window to be popped up when we are asked for a bid.
 * As of v0.6, this dialog is nonmodal.
 * @author James Ranson
 * @since v0.6
 */
public class QueryWindow extends Dialog implements ActionListener {

    private Button okB;

    private TextField tf;

    private boolean ready;

    /**
	 * Construct a new query window.
	 * @param parent The parent window for this dialog.
	 * @param text The question to ask the user.
	 */
    public QueryWindow(Frame parent, String text) {
        super(parent, "Query", false);
        setLayout(new GridLayout(3, 1));
        add(new Label(text, Label.LEFT));
        tf = new TextField(10);
        add(tf);
        okB = new Button("O.K.");
        add(okB);
        okB.addActionListener(this);
        tf.addActionListener(this);
        pack();
        show();
    }

    public void deactivate() {
        okB.removeActionListener(this);
        tf.removeActionListener(this);
        dispose();
    }

    /**
	 * Wait for the user to type something in.
	 * @param howlong Maximum wait time, in milliseconds.
	 * @return The string the user typed in, or null if timeout occurred.
	 */
    public synchronized String waitForAnswer(long howlong) throws InterruptedException {
        if (!ready) {
            wait(howlong);
        }
        if (ready) return tf.getText();
        return null;
    }

    public void actionPerformed(ActionEvent ev) {
        ready = true;
        deactivate();
        synchronized (this) {
            notifyAll();
        }
    }
}
