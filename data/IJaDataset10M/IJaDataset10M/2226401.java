package seco.gui.menu;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class UpdatablePopupMenu extends JPopupMenu {

    private static final long serialVersionUID = 3296559695629374971L;

    public void update() {
        int c = getComponentCount();
        for (int i = 0; i < c; i++) {
            Component m = getComponent(i);
            if (m instanceof JMenuItem) {
                JMenuItem mi = (JMenuItem) m;
                Action a = mi.getAction();
                if (a != null) mi.setEnabled(a.isEnabled());
            }
        }
    }
}
