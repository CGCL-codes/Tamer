package org.carabiner.harness;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Calls notify on the given object when the window closes.
 * 
 * @author Ben Rady (benrady@gmail.com)
 * 
 */
class WindowNotifier extends WindowAdapter {

    private Object m_Notifyee;

    WindowNotifier(Object notifyee) {
        m_Notifyee = notifyee;
    }

    public void windowClosed(WindowEvent e) {
        synchronized (m_Notifyee) {
            m_Notifyee.notify();
        }
    }
}
