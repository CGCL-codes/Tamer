package de.sciss.gui;

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import de.sciss.app.AbstractWindow;

/**
 *  @author		Hanns Holger Rutz
 *  @version	0.70, 19-Mar-08
 */
public class WindowListenerWrapper implements WindowListener {

    private final AbstractWindow.Listener l;

    private final AbstractWindow w;

    private WindowListenerWrapper(AbstractWindow.Listener l, AbstractWindow w) {
        this.l = l;
        this.w = w;
    }

    public static void add(AbstractWindow.Listener l, AbstractWindow w) {
        final WindowListenerWrapper wlw = new WindowListenerWrapper(l, w);
        ((Window) w.getWindow()).addWindowListener(wlw);
    }

    public static void remove(AbstractWindow.Listener l, AbstractWindow w) {
        final Window w2 = (Window) w.getWindow();
        final WindowListener[] coll = w2.getWindowListeners();
        WindowListenerWrapper wlw;
        for (int i = 0; i < coll.length; i++) {
            if (coll[i] instanceof WindowListenerWrapper) {
                wlw = (WindowListenerWrapper) coll[i];
                if (wlw.l == l) {
                    w2.removeWindowListener(wlw);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Listener was not registered " + l);
    }

    public void windowOpened(WindowEvent e) {
        l.windowOpened(wrap(e));
    }

    public void windowClosing(WindowEvent e) {
        l.windowClosing(wrap(e));
    }

    public void windowClosed(WindowEvent e) {
        l.windowClosed(wrap(e));
    }

    public void windowIconified(WindowEvent e) {
        l.windowIconified(wrap(e));
    }

    public void windowDeiconified(WindowEvent e) {
        l.windowDeiconified(wrap(e));
    }

    public void windowActivated(WindowEvent e) {
        l.windowActivated(wrap(e));
    }

    public void windowDeactivated(WindowEvent e) {
        l.windowDeactivated(wrap(e));
    }

    private AbstractWindow.Event wrap(WindowEvent e) {
        return AbstractWindow.Event.convert(w, e);
    }
}
