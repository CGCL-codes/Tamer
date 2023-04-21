package rene.gui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JPanel;

/**
 * A dialog, which can be closed by clicking on the close window field (a cross
 * on the top right corner in Windows 95), or by pressing the escape key.
 * <p>
 * Moreover, the dialog is a DoActionListener, which makes it possible to use
 * the simplified TextFieldAction etc.
 */
public class CloseDialog extends javax.swing.JDialog implements WindowListener, ActionListener, DoActionListener, KeyListener, FocusListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    boolean Dispose = true;

    public boolean Aborted = false;

    Frame F;

    public String Subject = "";

    public CloseDialog(final Frame f, final String s, final boolean modal) {
        super(f, s, modal);
        F = f;
        if (Global.ControlBackground != null) setBackground(Global.ControlBackground);
        addWindowListener(this);
        addKeyListener(this);
        addFocusListener(this);
    }

    public void windowActivated(final WindowEvent e) {
    }

    public void windowClosed(final WindowEvent e) {
    }

    public void windowClosing(final WindowEvent e) {
        if (close()) doclose();
    }

    public void windowDeactivated(final WindowEvent e) {
    }

    public void windowDeiconified(final WindowEvent e) {
    }

    public void windowIconified(final WindowEvent e) {
    }

    public void windowOpened(final WindowEvent e) {
    }

    /**
	 * @return true if the dialog is closed.
	 */
    public boolean close() {
        return true;
    }

    /**
	 * Calls close(), when the escape key is pressed.
	 * 
	 * @return true if the dialog may close.
	 */
    public boolean escape() {
        return close();
    }

    public ActionEvent E;

    public void actionPerformed(final ActionEvent e) {
        E = e;
        doAction(e.getActionCommand());
    }

    public void doAction(final String o) {
        if ("Close".equals(o) && close()) {
            Aborted = true;
            doclose();
        } else if (o.equals("Help")) {
            showHelp();
        }
    }

    public void showHelp() {
    }

    public void itemAction(final String o, final boolean flag) {
    }

    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && escape()) doclose();
    }

    public void keyReleased(final KeyEvent e) {
    }

    public void keyTyped(final KeyEvent e) {
    }

    /**
	 * Closes the dialog. This may be used in subclasses to do some action. Then
	 * call super.doclose()
	 */
    public void doclose() {
        setVisible(false);
        final Thread t = new Thread() {

            @Override
            public void run() {
                if (Dispose) dispose();
            }
        };
        t.start();
    }

    public void center(final Frame f) {
        final Dimension si = f.getSize(), d = getSize(), dscreen = getToolkit().getScreenSize();
        final Point lo = f.getLocation();
        int x = lo.x + si.width / 2 - d.width / 2;
        int y = lo.y + si.height / 2 - d.height / 2;
        if (x + d.width > dscreen.width) x = dscreen.width - d.width - 10;
        if (x < 10) x = 10;
        if (y + d.height > dscreen.height) y = dscreen.height - d.height - 10;
        if (y < 10) y = 10;
        setLocation(x, y);
    }

    public static void center(final Frame f, final Dialog dialog) {
        final Dimension si = f.getSize(), d = dialog.getSize(), dscreen = f.getToolkit().getScreenSize();
        final Point lo = f.getLocation();
        int x = lo.x + si.width / 2 - d.width / 2;
        int y = lo.y + si.height / 2 - d.height / 2;
        if (x + d.width > dscreen.width) x = dscreen.width - d.width - 10;
        if (x < 10) x = 10;
        if (y + d.height > dscreen.height) y = dscreen.height - d.height - 10;
        if (y < 10) y = 10;
        dialog.setLocation(x, y);
    }

    public void centerOut(final Frame f) {
        final Dimension si = f.getSize(), d = getSize(), dscreen = getToolkit().getScreenSize();
        final Point lo = f.getLocation();
        int x = lo.x + si.width - getSize().width + 20;
        int y = lo.y + si.height / 2 + 40;
        if (x + d.width > dscreen.width) x = dscreen.width - d.width - 10;
        if (x < 10) x = 10;
        if (y + d.height > dscreen.height) y = dscreen.height - d.height - 10;
        if (y < 10) y = 10;
        setLocation(x, y);
    }

    public void center() {
        final Dimension d = getSize(), dscreen = getToolkit().getScreenSize();
        setLocation((dscreen.width - d.width) / 2, (dscreen.height - d.height) / 2);
    }

    /**
	 * Note window position in Global.
	 */
    public void notePosition(final String name) {
        final Point l = getLocation();
        final Dimension d = getSize();
        Global.setParameter(name + ".x", l.x);
        Global.setParameter(name + ".y", l.y);
        Global.setParameter(name + ".w", d.width);
        if (d.height - Global.getParameter(name + ".h", 0) != 19) Global.setParameter(name + ".h", d.height);
    }

    /**
	 * Set window position and size.
	 */
    public void setPosition(final String name) {
        final Point l = getLocation();
        final Dimension d = getSize();
        final Dimension dscreen = getToolkit().getScreenSize();
        int x = Global.getParameter(name + ".x", l.x);
        int y = Global.getParameter(name + ".y", l.y);
        int w = Global.getParameter(name + ".w", d.width);
        int h = Global.getParameter(name + ".h", d.height);
        if (w > dscreen.width) w = dscreen.width;
        if (h > dscreen.height) h = dscreen.height;
        if (x < 0) x = 0;
        if (x + w > dscreen.width) x = dscreen.width - w;
        if (y < 0) y = 0;
        if (y + h > dscreen.height) y = dscreen.height - h;
        setLocation(x, y);
        setSize(w, h);
    }

    /**
	 * Override to set the focus somewhere.
	 */
    public void focusGained(final FocusEvent e) {
    }

    public void focusLost(final FocusEvent e) {
    }

    /**
	 * Note window size in Global.
	 */
    public void noteSize(final String name) {
        final Dimension d = getSize();
        Global.setParameter(name + ".w", d.width);
        Global.setParameter(name + ".h", d.height);
    }

    /**
	 * Set window size.
	 */
    public void setSize(final String name) {
        if (!Global.haveParameter(name + ".w")) pack(); else {
            final Dimension d = getSize();
            final int w = Global.getParameter(name + ".w", d.width);
            final int h = Global.getParameter(name + ".h", d.height);
            setSize(w, h);
        }
    }

    /**
	 * This inihibits dispose(), when the dialog is closed.
	 */
    public void setDispose(final boolean flag) {
        Dispose = flag;
    }

    public boolean isAborted() {
        return Aborted;
    }

    /**
	 * To add a help button to children.
	 * 
	 * @param p
	 * @param subject
	 */
    public void addHelp(final JPanel p, final String subject) {
        p.add(new MyLabel(""));
        p.add(new ButtonAction(this, Global.name("help"), "Help"));
        Subject = subject;
    }
}
