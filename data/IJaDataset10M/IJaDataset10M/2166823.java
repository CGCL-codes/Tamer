package org.homeunix.thecave.moss.swing.menu;

import java.awt.Component;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import org.homeunix.thecave.moss.swing.window.AbstractFrame;

public abstract class AbstractMenuBar extends JMenuBar implements StandardMenu {

    private final Collection<StandardMenu> subMenuItems = new LinkedList<StandardMenu>();

    private final AbstractFrame frame;

    public AbstractMenuBar(AbstractFrame frame) {
        super();
        this.frame = frame;
    }

    public void updateMenus() {
        for (StandardMenu item : subMenuItems) {
            item.updateMenus();
        }
    }

    @Override
    public JMenu add(JMenu arg0) {
        if (arg0 instanceof StandardMenu) {
            subMenuItems.add((StandardMenu) arg0);
        }
        return super.add(arg0);
    }

    @Override
    public void remove(Component comp) {
        if (comp instanceof StandardMenu) {
            subMenuItems.add((StandardMenu) comp);
        }
        super.remove(comp);
    }

    public AbstractFrame getFrame() {
        return frame;
    }
}
