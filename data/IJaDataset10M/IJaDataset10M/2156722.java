package com.google.code.appengine.awt;

import org.apache.harmony.awt.wtk.NativeWindow;
import com.google.code.appengine.awt.Component;
import com.google.code.appengine.awt.Container;
import com.google.code.appengine.awt.Graphics;
import com.google.code.appengine.awt.KeyboardFocusManager;
import com.google.code.appengine.awt.Point;
import com.google.code.appengine.awt.Toolkit;
import com.google.code.appengine.awt.Window;

/**
 * Heavyweight component specific behaviour
 */
class HWBehavior implements ComponentBehavior {

    private final Component component;

    private NativeWindow nativeWindow;

    HWBehavior(Component comp) {
        component = comp;
        Toolkit.checkHeadless();
        component.dispatchToIM = false;
    }

    public boolean isOpaque() {
        return true;
    }

    public boolean isLightweight() {
        return false;
    }

    public void setVisible(boolean b) {
        if (nativeWindow != null) {
            nativeWindow.setVisible(b);
        }
    }

    public NativeWindow getNativeWindow() {
        return nativeWindow;
    }

    public void addNotify() {
        nativeWindow = createNativeWindow();
        nativeWindow.setVisible(component.isVisible());
        updateFocus();
    }

    protected NativeWindow createNativeWindow() {
        return component.toolkit.createNativeWindow(component);
    }

    private void updateFocus() {
        if (component.isShowing()) {
            Window wnd = component.getWindowAncestor();
            if ((wnd == null) || (!wnd.isFocused() && !wnd.isActive())) {
                return;
            }
            KeyboardFocusManager.getCurrentKeyboardFocusManager().requestFocusInWindow(wnd, true);
        }
    }

    public Graphics getGraphics(int translationX, int translationY, int width, int height) {
        return component.toolkit.getGraphicsFactory().getGraphics2D(nativeWindow, translationX, translationY, width, height);
    }

    public void setBounds(int x, int y, int w, int h, int bMask) {
        if (nativeWindow != null) {
            Point loc = new Point(x, y);
            if (!(component instanceof Window)) {
                Component parent = component.getHWAncestor();
                loc = MouseDispatcher.convertPoint(component, 0, 0, parent);
            }
            nativeWindow.setBounds(loc.x, loc.y, w, h, bMask);
        }
    }

    public boolean isDisplayable() {
        return (nativeWindow != null);
    }

    public void setEnabled(boolean value) {
        if (nativeWindow != null) {
            nativeWindow.setEnabled(value);
        }
    }

    public void removeNotify() {
        if (nativeWindow == null) {
            return;
        }
        component.toolkit.removeNativeWindow(nativeWindow);
        NativeWindow temp = nativeWindow;
        nativeWindow = null;
        if (!(component instanceof Window)) {
            Component hwAncestor = component.getHWAncestor();
            if ((hwAncestor == null) || !hwAncestor.isDisplayable()) {
                return;
            }
        }
        temp.dispose();
    }

    public void onMove(int x, int y) {
        setBounds(component.x, component.y, component.w, component.h, NativeWindow.BOUNDS_NOSIZE);
    }

    public void setZOrder(int newIndex, int oldIndex) {
        NativeWindow win = null;
        Container par = component.getParent();
        if (par != null) {
            int size = par.getComponentCount();
            for (int i = Math.min(newIndex - 1, size - 1); i >= 0; i--) {
                Component comp = par.getComponent(i);
                if (!comp.isLightweight() && comp.isDisplayable()) {
                    win = comp.getNativeWindow();
                    break;
                }
            }
        }
        if (nativeWindow != null) {
            nativeWindow.placeAfter(win);
        }
    }

    public boolean setFocus(boolean focus, Component opposite) {
        if (nativeWindow != null) {
            return nativeWindow.setFocus(focus);
        }
        return false;
    }
}
