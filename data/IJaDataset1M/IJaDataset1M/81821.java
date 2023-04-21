package org.objectstyle.jstaple.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import ognl.OgnlException;

/**
 * Unorganized utility methods.
 * 
 * @author Andrei Adamchik
 */
public final class STUtil {

    /**
     * Borrowed from Scope. Copyright: The Scope Team.
     */
    public static void centerOnWindow(Window parent, Window window) {
        if (parent == null) {
            throw new IllegalArgumentException("Can't center on a null parent.");
        }
        if (window == null) {
            throw new IllegalArgumentException("Can't center a null Window.");
        }
        Dimension parentSize = parent.getSize();
        Dimension windowSize = window.getSize();
        Point parentLocation = new Point(0, 0);
        if (parent.isShowing()) {
            parentLocation = parent.getLocationOnScreen();
        }
        int x = parentLocation.x + parentSize.width / 2 - windowSize.width / 2;
        int y = parentLocation.y + parentSize.height / 2 - windowSize.height / 2;
        window.setLocation(x, y);
    }

    public static void makeCloseableOnESC(final JDialog dialog) {
        KeyStroke escReleased = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        ActionListener closeAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (dialog.isVisible()) {
                    WindowEvent windowClosing = new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING);
                    WindowListener[] listeners = dialog.getWindowListeners();
                    if (listeners != null && listeners.length > 0) {
                        for (int i = 0; i < listeners.length; i++) {
                            listeners[i].windowClosing(windowClosing);
                        }
                    }
                }
            }
        };
        dialog.getRootPane().registerKeyboardAction(closeAction, escReleased, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * Returns a root cause of a chained exception.
     */
    public static Throwable unwind(Throwable th) {
        if (th.getCause() != null) {
            return unwind(th.getCause());
        } else if (th instanceof OgnlException) {
            Throwable reason = ((OgnlException) th).getReason();
            return (reason != null) ? unwind(reason) : th;
        }
        return th;
    }

    private STUtil() {
    }
}
