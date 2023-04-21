package net.sf.logshark.simple;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;

class DialogDisposingAndOwnerNotifyingHandler extends WindowAdapter {

    private JDialog m_dialog;

    private Object m_notificationLock;

    public void windowClosing(WindowEvent event) {
        m_dialog.dispose();
        synchronized (m_notificationLock) {
            m_notificationLock.notifyAll();
        }
    }

    DialogDisposingAndOwnerNotifyingHandler(JDialog dialog, Object notificationLock) {
        super();
        m_dialog = dialog;
        m_notificationLock = notificationLock;
    }
}
