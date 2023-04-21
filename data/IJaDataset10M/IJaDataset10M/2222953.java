package org.armedbear.lisp.java.awt;

import org.armedbear.lisp.JHandler;
import java.awt.Component;
import java.awt.event.ComponentEvent;

public class ComponentAdapter extends java.awt.event.ComponentAdapter {

    public static synchronized void addTo(Component component) {
        component.addComponentListener(new ComponentAdapter());
    }

    private void call(String s, ComponentEvent componentevent) {
        JHandler.callLisp(s, componentevent.getComponent(), componentevent.paramString());
    }

    public void componentHidden(ComponentEvent componentevent) {
        call("COMPONENTHIDDEN", componentevent);
    }

    public void componentMoved(ComponentEvent componentevent) {
        call("COMPONENTMOVED", componentevent);
    }

    public void componentResized(ComponentEvent componentevent) {
        call("COMPONENTRESIZED", componentevent);
    }

    public void componentShown(ComponentEvent componentevent) {
        call("COMPONENTSHOWN", componentevent);
    }
}
