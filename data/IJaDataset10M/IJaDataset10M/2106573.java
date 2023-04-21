package views.background.ghost;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import javax.swing.SwingUtilities;
import views.background.EmptyFrame;
import views.background.RightSideBar;

public abstract class AbstractGhostDropManager implements GhostDropListener {

    protected Component component;

    protected LinkedList<GhostDroppable> targets;

    public AbstractGhostDropManager() {
        this(null);
    }

    public AbstractGhostDropManager(Component component) {
        this(component, null);
    }

    public AbstractGhostDropManager(Component component, LinkedList<GhostDroppable> targets) {
        this.component = component;
        this.targets = targets;
    }

    public void addTarget(GhostDroppable target) {
        if (targets == null) targets = new LinkedList<GhostDroppable>();
        targets.add(target);
    }

    public void removeTarget(GhostDroppable target) {
        if (targets != null) targets.remove(target);
    }

    protected Point getTranslatedPoint(Point point, Component c) {
        Point p = (Point) point.clone();
        SwingUtilities.convertPointFromScreen(p, c);
        return p;
    }

    protected LinkedList<GhostDroppable> isInTarget(Point point) {
        if (targets == null) return new LinkedList<GhostDroppable>();
        LinkedList<GhostDroppable> res = new LinkedList<GhostDroppable>();
        for (GhostDroppable target : targets) {
            Point p = getTranslatedPoint(point, (Component) target);
            if (target instanceof EmptyFrame) {
                EmptyFrame frame = (EmptyFrame) target;
                p.x = p.x + frame.getX();
                p.y = p.y + frame.getY();
            }
            if (target instanceof RightSideBar) {
                RightSideBar frame = (RightSideBar) target;
                p.x = p.x + frame.getX();
                p.y = p.y + frame.getY();
            }
            Rectangle bounds = target.getDropZoneBounds();
            if (bounds.contains(p)) res.add(target);
        }
        return res;
    }

    public void ghostDropped(GhostDropEvent e) {
    }

    /**
	 * @return the component
	 */
    public Component getComponent() {
        return component;
    }
}
