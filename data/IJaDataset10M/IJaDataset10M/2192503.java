package com.vividsolutions.jump.workbench.ui;

import java.awt.Component;
import java.awt.event.KeyListener;

/** 
 * {@link RecursiveListener} extended to watch for global function key events.
 */
public abstract class RecursiveKeyListener extends RecursiveListener implements KeyListener {

    public RecursiveKeyListener(Component component) {
        super(component);
    }

    public void addListenerTo(Component comp) {
        comp.addKeyListener(this);
    }

    public void removeListenerFrom(Component comp) {
        comp.removeKeyListener(this);
    }
}
