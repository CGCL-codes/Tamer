package jgm.gui.dialogs;

import jgm.gui.GUI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public abstract class Dialog extends JDialog {

    public static final int PADDING = jgm.gui.GUI.PADDING;

    protected GUI gui;

    protected String title;

    protected GridBagConstraints c = new GridBagConstraints();

    public Dialog(GUI gui, String title) {
        super(gui.frame, title, true);
        this.gui = gui;
        JPanel spacer = new JPanel(new BorderLayout());
        spacer.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        setContentPane(spacer);
        this.title = title;
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        this.addWindowListener(new WindowAdapter() {

            public void windowActivated(WindowEvent e) {
                onShow();
            }
        });
    }

    protected final void makeVisible() {
        validate();
        pack();
        setLocationRelativeTo(null);
    }

    protected void onShow() {
        if (jgm.JGlideMon.debug) java.util.logging.Logger.getLogger("jgm").fine("Showing dialog " + getClass().getName());
    }

    /**
	 * Returns a JPanel with the supplied buttons nicely
	 * laid out in a row.
	 */
    public static final JPanel makeNiceButtons(JButton... buttons) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(PADDING, 0, 0, 0));
        JPanel p1 = new JPanel(new GridLayout(1, 0, 10, 0));
        p1.setOpaque(false);
        for (JButton b : buttons) {
            p1.add(b);
        }
        p.add(p1);
        return p;
    }
}
