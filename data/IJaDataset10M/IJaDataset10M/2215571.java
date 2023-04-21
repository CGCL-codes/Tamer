package org.form4j.form.field.util;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;

/**
 * Static utility methods for focus traversal manipulation.
 *
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.5 $ $Date: 2005/08/06 16:42:16 $
 **/
public final class FocusTraversalHelper {

    private FocusTraversalHelper() {
    }

    /**
     * Set tabtraversal for a component.
     */
    public static void setTabTraversalActive(final JComponent component, boolean editable) {
        if (!editable) {
            component.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardTab);
            component.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardTab);
        } else {
            component.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardControlTab);
            component.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardControlTab);
        }
    }

    static final Logger LOG = Logger.getLogger(FocusTraversalHelper.class.getName());

    private static HashSet forwardTab = new HashSet();

    static {
        forwardTab.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
    }

    private static HashSet backwardTab = new HashSet();

    static {
        backwardTab.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK));
    }

    private static HashSet forwardControlTab = new HashSet();

    static {
        forwardTab.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_MASK));
    }

    private static HashSet backwardControlTab = new HashSet();

    static {
        backwardTab.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK | KeyEvent.CTRL_MASK));
    }

    private static HashSet emptySet = new HashSet();
}
