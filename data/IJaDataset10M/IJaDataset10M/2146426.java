package wjhk.jupload2.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import wjhk.jupload2.exception.JUploadIOException;
import wjhk.jupload2.policies.UploadPolicy;

/**
 * Global applet popup menu. It currently contains only the debug on/off menu
 * entry.
 */
final class JUploadDebugPopupMenu extends JPopupMenu implements ActionListener, ItemListener, PopupMenuListener {

    /** A generated serialVersionUID, to avoid warning during compilation */
    private static final long serialVersionUID = -5473337111643079720L;

    /**
     * Identifies the menu item that will set debug mode on or off (on means:
     * debugLevel=100)
     */
    JCheckBoxMenuItem cbmiDebugOnOff = null;

    JCheckBoxMenuItem cbmiLogWindowOnOff = null;

    JMenuItem jMenuItemViewLastResponseBody = null;

    JMenuItem jMenuItemClearLogWindowContent = null;

    JMenuItem jMenuItemCopyLogWindowContent = null;

    /**
     * The current upload policy.
     */
    private UploadPolicy uploadPolicy;

    JUploadDebugPopupMenu(UploadPolicy uploadPolicy) {
        this.uploadPolicy = uploadPolicy;
        this.addPopupMenuListener(this);
        this.cbmiDebugOnOff = new JCheckBoxMenuItem("Debug enabled");
        this.cbmiDebugOnOff.setState(this.uploadPolicy.getDebugLevel() == 100);
        add(this.cbmiDebugOnOff);
        this.cbmiDebugOnOff.addItemListener(this);
        this.cbmiLogWindowOnOff = new JCheckBoxMenuItem("Show log window");
        this.cbmiLogWindowOnOff.setState(this.uploadPolicy.getShowLogWindow().equals("true"));
        add(this.cbmiLogWindowOnOff);
        this.cbmiLogWindowOnOff.addItemListener(this);
        this.jMenuItemClearLogWindowContent = new JMenuItem("Clear the log window content");
        add(this.jMenuItemClearLogWindowContent);
        this.jMenuItemClearLogWindowContent.addActionListener(this);
        this.jMenuItemCopyLogWindowContent = new JMenuItem("Copy the log window content");
        add(this.jMenuItemCopyLogWindowContent);
        this.jMenuItemCopyLogWindowContent.addActionListener(this);
        this.jMenuItemViewLastResponseBody = new JMenuItem("View last response body");
        add(this.jMenuItemViewLastResponseBody);
        this.jMenuItemViewLastResponseBody.addActionListener(this);
    }

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (this.cbmiDebugOnOff == e.getItem()) {
            this.uploadPolicy.setDebugLevel((this.cbmiDebugOnOff.isSelected() ? 100 : 0));
        } else if (this.cbmiLogWindowOnOff == e.getItem()) {
            if (this.cbmiLogWindowOnOff.isSelected()) {
                this.uploadPolicy.setShowLogWindow(UploadPolicy.SHOWLOGWINDOW_TRUE);
            } else {
                this.uploadPolicy.setShowLogWindow(UploadPolicy.SHOWLOGWINDOW_FALSE);
            }
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (this.jMenuItemViewLastResponseBody == e.getSource()) {
            try {
                new DebugDialog(null, this.uploadPolicy.getLastResponseBody(), this.uploadPolicy);
            } catch (JUploadIOException e1) {
                this.uploadPolicy.displayErr(e1);
            }
        } else if (this.jMenuItemClearLogWindowContent == e.getSource()) {
            this.uploadPolicy.getContext().getUploadPanel().clearLogWindow();
        } else if (this.jMenuItemCopyLogWindowContent == e.getSource()) {
            this.uploadPolicy.getContext().getUploadPanel().copyLogWindow();
        }
    }

    /** @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent) */
    public void popupMenuCanceled(PopupMenuEvent arg0) {
    }

    /** @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent) */
    public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
    }

    /**
     * Set the "View last response body" menu enabled or disabled.
     * 
     * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
     */
    public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
        String s = this.uploadPolicy.getLastResponseBody();
        this.jMenuItemViewLastResponseBody.setEnabled(s != null && !s.equals(""));
    }
}
