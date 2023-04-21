package org.designerator.common.action;

import org.designerator.common.interfaces.IActionAdapter;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

public class ActionUtil {

    public static Menu getSubMenu(Menu menu, String name) {
        MenuItem i1 = new MenuItem(menu, SWT.MENU);
        i1.setText(name);
        Menu m1 = new Menu(menu);
        i1.setMenu(m1);
        return m1;
    }

    public static void addAction(Menu menu, IAction o) {
        ActionContributionItem item = new ActionContributionItem(o);
        item.fill(menu, -1);
    }

    public static void showActionMenu(Event toolItemEvent, final String toolTipAsId, final IActionAdapter pd) {
        Widget widget = toolItemEvent.widget;
        if (widget != null && widget instanceof ToolItem) {
            ToolItem item = (ToolItem) widget;
            ToolBar bar = item.getParent();
            ToolItem[] items = bar.getItems();
            for (int i = 0; i < items.length; i++) {
                if (toolTipAsId.equals(items[i].getToolTipText())) {
                    Rectangle rect = items[i].getBounds();
                    Point p = new Point(rect.x + rect.width, rect.y);
                    p = bar.toDisplay(p);
                    Menu menu = new Menu(bar);
                    pd.fillActionMenu(menu);
                    menu.setLocation(p.x, p.y);
                    menu.setVisible(true);
                    break;
                }
            }
        }
    }
}
