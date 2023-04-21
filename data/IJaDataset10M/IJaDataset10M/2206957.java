package com.apelon.beans.apeldlg;

import java.awt.*;
import java.awt.event.*;
import com.apelon.beans.apelconnection.*;
import com.apelon.beans.apelsearch.*;
import com.apelon.beans.apelpanel.*;
import javax.swing.*;

public class ApelDlg extends javax.swing.JDialog implements ApelPanelListener {

    protected ApelPanel apelPanel = null;

    protected boolean panelSet = false;

    /**
 * ApelDlg constructor comment.
 */
    public ApelDlg(ApelPanel ap) {
        super();
        setApelPanel(ap);
    }

    /**
 * ApelDlg constructor comment.
 */
    public ApelDlg(ApelPanel ap, Component centerInThis) {
        super();
        setApelPanel(ap);
        centerInWindow(centerInThis);
    }

    /**
 * Insert the method's description here.
 * Creation date: (2/8/00 1:10:42 PM)
 * @param wnd java.awt.Window
 */
    public void centerInWindow(java.awt.Component parent) {
        int dlgx, dlgy;
        Point parentLoc;
        Dimension parentSize;
        Dimension dlgSize = this.getSize();
        if (parent == null) {
            parentLoc = new Point(0, 0);
            parentSize = Toolkit.getDefaultToolkit().getScreenSize();
        } else {
            parentSize = parent.getSize();
            parentLoc = parent.getLocationOnScreen();
        }
        dlgx = parentLoc.x + ((parentSize.width - dlgSize.width) / 2);
        dlgy = parentLoc.y + ((parentSize.height - dlgSize.height) / 2);
        this.setLocation(dlgx, dlgy);
    }

    /**
 * Insert the method's description here.
 * Creation date: (1/30/00 1:36:28 PM)
 */
    public void panelComplete() {
        this.dispose();
    }

    /**
 * Insert the method's description here.
 * Creation date: (2/8/00 12:24:07 PM)
 * @param ap com.apelon.beans.apelpanel.ApelPanel
 */
    public boolean setApelPanel(ApelPanel ap) {
        if (panelSet) return false;
        panelSet = true;
        apelPanel = ap;
        this.setSize(apelPanel.getWidth() + 30, apelPanel.getHeight() + 30);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(apelPanel, BorderLayout.CENTER);
        this.setResizable(false);
        this.setModal(true);
        this.setTitle(apelPanel.getTitle());
        getRootPane().setDefaultButton(apelPanel.getDefaultButton());
        apelPanel.addApelPanelListener(this);
        return true;
    }

    /**
 * Call this method to show the dialog. This will return whatever the
 * underlying panel returns as its result.
 *
 * Creation date: (1/30/00 1:09:03 PM)
 * @return int
 */
    public int showApelDlg() {
        this.show();
        return apelPanel.getResult();
    }

    /**
 * Insert the method's description here.
 * Creation date: (5/18/2001 10:06:15 AM)
 */
    public ApelDlg(ApelPanel ap, Component centerInThis, JFrame parent) {
        super(parent, false);
        setApelPanel(ap);
        centerInWindow(centerInThis);
    }

    public ApelDlg(ApelPanel ap, Component centerInThis, Dialog parent) {
        super(parent, false);
        setApelPanel(ap);
        centerInWindow(centerInThis);
    }

    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane;
    }
}
