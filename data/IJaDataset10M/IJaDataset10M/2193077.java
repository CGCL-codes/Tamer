package org.adapit.wctoolkit.infrastructure.treecontrollers;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class AutoScrollingJTree extends JTree implements Autoscroll {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8461949862346436L;

    private int margin = 18;

    public AutoScrollingJTree() {
        super();
    }

    public void autoscroll(Point p) {
        try {
            int realrow = getRowForLocation(p.x, p.y);
            Rectangle outer = getBounds();
            realrow = (p.y + outer.y <= margin ? realrow < 1 ? 0 : realrow - 1 : realrow < getRowCount() - 1 ? realrow + 1 : realrow);
            scrollRowToVisible(realrow);
            expandRow(realrow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Insets getAutoscrollInsets() {
        try {
            Rectangle outer = getBounds();
            Rectangle inner = getParent().getBounds();
            return new Insets(inner.y - outer.y + margin, inner.x - outer.x + margin, outer.height - inner.height - inner.y + outer.y + margin, outer.width - inner.width - inner.x + outer.x + margin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.getInsets();
    }

    @Override
    public void startEditingAtPath(TreePath arg0) {
        try {
            super.setEditable(true);
            super.startEditingAtPath(arg0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
