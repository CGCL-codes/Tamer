package org.jumpmind.pos.ui.jfc.screen;

import java.awt.FlowLayout;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jumpmind.pos.IMenuManager;
import org.jumpmind.pos.IPOSContext;
import org.jumpmind.pos.MessageType;
import org.jumpmind.pos.ui.MenuItemTreeNode;
import org.jumpmind.pos.ui.MenuItemType;
import org.jumpmind.pos.ui.jfc.common.UIHelper;
import org.jumpmind.pos.ui.jfc.widget.MenuAction;

public class ActionBar extends JPanel {

    private static final long serialVersionUID = 1L;

    public ActionBar() {
        setOpaque(false);
    }

    public void display(IPOSContext context, IMenuManager activityManager) {
        List<MenuItemTreeNode> actionItems = context.getCurrentMenuLocation().getChildrenNodes(MenuItemType.ACTION, context);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        UIHelper.removeComponentsOfType(new Class[] { JButton.class, Box.Filler.class }, this);
        for (MenuItemTreeNode item : actionItems) {
            add(UIHelper.makeTouchable(new JButton(new MenuAction(context.getMessage(MessageType.OPERATOR, item.getMenuItem().getNameId()), item, activityManager))));
        }
        this.add(Box.createHorizontalGlue());
        this.revalidate();
        this.repaint();
    }
}
