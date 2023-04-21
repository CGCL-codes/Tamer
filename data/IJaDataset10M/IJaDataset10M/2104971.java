package au.edu.qut.yawl.editor.swing.menu;

import au.edu.qut.yawl.editor.actions.palette.*;
import javax.swing.JPopupMenu;

public class PalettePopupMenu extends JPopupMenu {

    public PalettePopupMenu() {
        super();
        addMenuItems();
    }

    private void addMenuItems() {
        add(new AtomicTaskAction());
        add(new CompositeTaskAction());
        add(new MultipleAtomicTaskAction());
        add(new MultipleCompositeTaskAction());
        add(new ConditionAction());
        add(new FlowRelationAction());
        add(new MarqueeAction());
    }
}
