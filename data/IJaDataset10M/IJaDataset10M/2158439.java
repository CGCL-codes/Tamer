package jpen.owner.awt;

import java.awt.Component;
import java.awt.geom.Point2D;
import java.awt.Point;
import javax.swing.SwingUtilities;
import jpen.owner.PenClip;

final class ComponentPenClip implements PenClip {

    final ComponentPenOwner componentPenOwner;

    public ComponentPenClip(ComponentPenOwner componentPenOwner) {
        this.componentPenOwner = componentPenOwner;
    }

    public void evalLocationOnScreen(Point pointOnScreen) {
        Component activeComponent = componentPenOwner.getActiveComponent();
        if (activeComponent == null) return;
        pointOnScreen.x = pointOnScreen.y = 0;
        SwingUtilities.convertPointToScreen(pointOnScreen, activeComponent);
    }

    public boolean contains(Point2D.Float point) {
        Component activeComponent = componentPenOwner.getActiveComponent();
        if (activeComponent == null) return false;
        if (point.x < 0 || point.y < 0 || point.x > activeComponent.getWidth() || point.y > activeComponent.getHeight()) {
            return false;
        }
        return true;
    }
}
