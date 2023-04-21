package com.bluemarsh.jswat.breakpoint.ui;

import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.panel.BreakPanel;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;

/**
 * Class ManagerDialog is responsible for building a dialog that
 * presents all of the breakpoint groups and breakpoints. It allows
 * the user to add new breakpoints, as well as edit or delete the
 * existing breakpoint groups and breakpoints.
 *
 * @author  Nathan Fiedler
 */
public class ManagerDialog extends JDialog {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /** Panel showing the breakpoints. */
    protected BreakPanel breakpointsPanel;

    /** Session this dialog is associated with. */
    protected Session owningSession;

    /**
     * Constructs the breakpoint managing dialog.
     *
     * @param  session  Owning session.
     * @param  window   Owning window.
     */
    public ManagerDialog(Session session, Frame owner) {
        super(owner, Bundle.getString("ManagerDialog.title"));
        owningSession = session;
        Container pane = getContentPane();
        breakpointsPanel = new BreakPanel();
        session.addListener(breakpointsPanel);
        breakpointsPanel.refresh(session);
        pane.add(breakpointsPanel.getUI(), "Center");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                owningSession.removeListener(breakpointsPanel);
                dispose();
            }
        });
        pack();
        if (getWidth() < 400) {
            setSize(400, getHeight());
        }
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
