package org.aiotrade.util.awt;

import java.awt.Component;
import java.awt.event.ComponentListener;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author Caoyuan Deng
 */
public class AWTUtil {

    public static final void removeAllAWTListenersOf(Component c) {
        for (MouseListener l : c.getMouseListeners()) {
            c.removeMouseListener(l);
        }
        for (MouseMotionListener l : c.getMouseMotionListeners()) {
            c.removeMouseMotionListener(l);
        }
        for (MouseWheelListener l : c.getMouseWheelListeners()) {
            c.removeMouseWheelListener(l);
        }
        for (KeyListener l : c.getKeyListeners()) {
            c.removeKeyListener(l);
        }
        for (ComponentListener l : c.getComponentListeners()) {
            c.removeComponentListener(l);
        }
        for (InputMethodListener l : c.getInputMethodListeners()) {
            c.removeInputMethodListener(l);
        }
        for (FocusListener l : c.getFocusListeners()) {
            c.removeFocusListener(l);
        }
    }
}
