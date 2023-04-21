package edu.upmc.opi.caBIG.caTIES.client.vr.utils.widgets;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 */
public class JMessagePane extends JPanel {

    /**
     * Field child.
     */
    protected JComponent child = null;

    protected JMessageBar msgBar;

    public static int MSG_TYPE_WARNING = JMessageBar.MSG_TYPE_WARNING;

    public static int MSG_TYPE_ERROR = JMessageBar.MSG_TYPE_ERROR;

    public static int MSG_TYPE_MESSAGE = JMessageBar.MSG_TYPE_MESSAGE;

    public static int MSG_TYPE_LOADING = JMessageBar.MSG_TYPE_LOADING;

    public JMessagePane() {
    }

    public JMessagePane(int msgtype, String message) {
        this(msgtype, message, new JPanel());
    }

    public JMessagePane(JComponent child) {
        this.child = child;
        msgBar = new JMessageBar();
        initGUI();
    }

    public JMessagePane(int msgtype, String message, JComponent child) {
        this.child = child;
        msgBar = new JMessageBar(msgtype, message);
        initGUI();
    }

    /**
     * Method initGUI.
     */
    private void initGUI() {
        this.setLayout(new BorderLayout());
        this.add(msgBar, BorderLayout.NORTH);
        this.add(child, BorderLayout.CENTER);
    }

    public void setMessage(int type, String msg) {
        if (msgBar == null) msgBar = new JMessageBar(JMessageBar.MSG_TYPE_MESSAGE, msg);
        msgBar.setMessage(type, msg);
    }

    public JMessageBar getMessageBar() {
        return msgBar;
    }

    public void hideMessageBar() {
        msgBar.setVisible(false);
    }

    public void showMessageBar() {
        msgBar.setVisible(true);
    }

    public JComponent getChildComponent() {
        return child;
    }

    public void setChildComponent(JComponent child) {
        this.child = child;
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(msgBar, BorderLayout.NORTH);
        this.add(child, BorderLayout.CENTER);
    }
}
