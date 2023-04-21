package com.cidero.swing;

import java.awt.Cursor;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.SwingUtilities;

/**
 * Convenience class for modifying miscellanous UI stuff from non-event
 * threads. Puts necessary action(s) on Swing event queue.
 */
public class MySwingUtil {

    public static void setCursorAsync(JComponent component, Cursor cursor) {
        SwingUtilities.invokeLater(new ChangeCursorThread(component, cursor));
    }

    static class ChangeCursorThread implements Runnable {

        JComponent component;

        Cursor cursor;

        public ChangeCursorThread(JComponent component, Cursor cursor) {
            this.component = component;
            this.cursor = cursor;
        }

        public void run() {
            component.setCursor(cursor);
        }
    }
}
