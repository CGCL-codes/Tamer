package br.org.databasetools.core.images;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import br.org.databasetools.core.view.window.ITabulable;

public class KeysManager {

    private static ITabulable tabulable;

    private static String[] keys = null;

    private static Set<AWTKeyStroke> forward = null;

    private static Set<AWTKeyStroke> backward = null;

    public static Set<? extends AWTKeyStroke> getForwardKeys(ITabulable obj) {
        return obj.getKeysTabular(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
    }

    public static void setForwardKeys(ITabulable obj, String[] k) {
        tabulable = obj;
        keys = k;
        forward = new HashSet<AWTKeyStroke>();
        tabulable.setKeysTabular(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forward);
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            if (key.equalsIgnoreCase("tab")) {
                forward.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
            } else if (key.equalsIgnoreCase("enter")) {
                forward.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
            } else if (key.equalsIgnoreCase("down")) {
                forward.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_DOWN, 0));
            } else if (keys.length == 1 && key.equalsIgnoreCase("all")) {
                forward.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
                forward.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
            }
        }
        tabulable.setKeysTabular(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forward);
    }

    public static void setForwardKeys(ITabulable obj, Set<? extends AWTKeyStroke> ks) {
        tabulable = obj;
        tabulable.setKeysTabular(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, ks);
    }

    public static Set<? extends AWTKeyStroke> getBackwardKeys(ITabulable obj) {
        return obj.getKeysTabular(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
    }

    public static void setBackwardKeys(ITabulable obj, String[] k) {
        tabulable = obj;
        keys = k;
        backward = new HashSet<AWTKeyStroke>();
        tabulable.setKeysTabular(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backward);
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            if (key.equalsIgnoreCase("shifttab")) {
                backward.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK));
            } else if (key.equalsIgnoreCase("up")) {
                backward.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_UP, 0));
            } else if (keys.length == 1 && key.equalsIgnoreCase("all")) {
                backward.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK));
            }
        }
        tabulable.setKeysTabular(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backward);
    }

    public static void setBackwardKeys(ITabulable obj, Set<? extends AWTKeyStroke> ks) {
        tabulable = obj;
        tabulable.setKeysTabular(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, ks);
    }
}
