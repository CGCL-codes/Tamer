package com.bluebrim.swing.client;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JMenu;

/**
 * Creation date: (2000-11-14 09:46:50)
 * @author: Dennis
 */
public abstract class CoComponentVisitor {

    public abstract void doit(Component c);

    public final void visit(Component c) {
        doit(c);
        if (c instanceof JMenu) {
            Component[] cs = ((JMenu) c).getMenuComponents();
            int I = cs.length;
            for (int i = 0; i < I; i++) {
                visit(cs[i]);
            }
        }
        if (c instanceof Container) {
            Component[] cs = ((Container) c).getComponents();
            int I = cs.length;
            for (int i = 0; i < I; i++) {
                visit(cs[i]);
            }
        }
    }
}
