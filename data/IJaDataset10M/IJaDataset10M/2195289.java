package view.extension;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.KeyStroke;

public class TransferFocus {

    /**
     * Patch the behaviour of a component. 
     * TAB transfers focus to the next focusable component,
     * SHIFT+TAB transfers focus to the previous focusable component.
     * 
     * @param c The component to be patched.
     */
    public static void patch(Component c) {
        Set<KeyStroke> strokes = new HashSet<KeyStroke>(Arrays.asList(KeyStroke.getKeyStroke("pressed TAB")));
        c.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
        strokes = new HashSet<KeyStroke>(Arrays.asList(KeyStroke.getKeyStroke("shift pressed TAB")));
        c.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
    }
}
