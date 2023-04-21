package org.ttt.salt.editor;

import java.io.*;
import java.util.*;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.ttt.salt.TBXFile;
import org.ttt.salt.editor.tbxedit.XMLErrorPane;

/**
 * @author Lance Finn Helsten
 * @version $Id: ErrorsWindow.java 1 2008-05-23 03:51:58Z lanhel $
 */
public class ErrorsWindow extends JFrame implements WindowListener {

    /** SCM information. */
    public static final String RCSID = "$Id: ErrorsWindow.java 1 2008-05-23 03:51:58Z lanhel $";

    /** resource bundle that is used by all menus. */
    private static ResourceBundle bundle;

    /** Menu Bar controller. */
    private ErrorsMenuBar mbar;

    /** The erros to list in the window. */
    private DefaultListModel errors = new javax.swing.DefaultListModel();

    /**
     * Get the bundle used for localization in this window.
     *
     * @return The localization {@link java.util.ResourceBundle} for this
     *  window.
     */
    public static ResourceBundle getBundle() {
        return bundle;
    }

    /**
     * This will create a new editor with the given File as the original.
     *
     * @param   file The TBXFile to edit.
     * @throws  IOException Any I/O exceptions that occur.
     */
    public ErrorsWindow(TBXFile file) throws IOException {
        super("Unnamed");
        this.addWindowListener(org.ttt.salt.editor.Main.getInstance());
        Iterator erriter = file.getInvalidatingExceptions().iterator();
        while (erriter.hasNext()) {
            Exception err = (Exception) erriter.next();
            if (err != null) errors.addElement(err.toString());
        }
        mbar = new ErrorsMenuBar(this);
        setJMenuBar(mbar.getJMenuBar());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        XMLErrorPane errorPane = new XMLErrorPane(null, errors);
        this.getContentPane().add(errorPane);
        this.setTitle("Errors");
        pack();
        addWindowListener(this);
        positionFrame();
    }

    /**
     * Send a message to close this window.
     */
    public final void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Get the menu bar used on this window.
     *
     * @return The menu bar for this window.
     */
    public final ErrorsMenuBar getErrorsMenuBar() {
        return mbar;
    }

    /** {@inheritDoc} */
    public void windowOpened(WindowEvent evt) {
    }

    /** {@inheritDoc} */
    public void windowClosed(WindowEvent evt) {
        removeWindowListener(this);
    }

    /** {@inheritDoc} */
    public void windowClosing(WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    /** {@inheritDoc} */
    public void windowActivated(WindowEvent evt) {
    }

    /** {@inheritDoc} */
    public void windowDeactivated(WindowEvent evt) {
    }

    /** {@inheritDoc} */
    public void windowDeiconified(WindowEvent evt) {
    }

    /** {@inheritDoc} */
    public void windowIconified(WindowEvent evt) {
    }

    /**
     * Position the frame in a resonable location on the screen.
     */
    private void positionFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    /**
     */
    static {
        bundle = ResourceBundle.getBundle("org.ttt.salt.editor.ErrorsWindow");
    }
}
