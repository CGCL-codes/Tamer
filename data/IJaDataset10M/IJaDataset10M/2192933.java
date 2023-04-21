package org.plazmaforge.studio.dbdesigner.parts;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.*;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.widgets.Control;

public class ERDScrollingGraphicalViewer extends ScrollingGraphicalViewer {

    public ERDScrollingGraphicalViewer() {
    }

    public void reveal(EditPart editpart) {
        if (editpart == null) return;
        for (EditPart editpart1 = editpart.getParent(); editpart1 != null; editpart1 = editpart1.getParent()) if (editpart1 instanceof IAdaptable) {
            EditPart editpart2 = editpart1;
            ExposeHelper exposehelper = (ExposeHelper) editpart2.getAdapter(org.eclipse.gef.ExposeHelper.class);
            if (exposehelper != null) exposehelper.exposeDescendant(editpart);
        }
        AccessibleEditPart accessibleeditpart = (AccessibleEditPart) editpart.getAdapter(org.eclipse.gef.AccessibleEditPart.class);
        if (accessibleeditpart != null) getControl().getAccessible().setFocus(accessibleeditpart.getAccessibleID());
        Viewport viewport = getFigureCanvas().getViewport();
        IFigure ifigure = ((GraphicalEditPart) editpart).getFigure();
        Rectangle rectangle = ifigure.getBounds().getCopy();
        for (ifigure = ifigure.getParent(); ifigure != null && ifigure != viewport; ifigure = ifigure.getParent()) ifigure.translateToParent(rectangle);
        Dimension dimension = viewport.getClientArea().getSize();
        int i = (dimension.width - rectangle.width - 1) / 2;
        int j = (dimension.height - rectangle.height - 1) / 2;
        i = i <= 5 ? 5 : i;
        j = j <= 5 ? 5 : j;
        Point point = rectangle.getTopLeft();
        Point point1 = rectangle.getBottomRight().translate(dimension.getNegated());
        Point point2 = new Point();
        if (dimension.width < rectangle.width) point2.x = Math.min(point1.x, Math.max(point.x, viewport.getViewLocation().x)); else point2.x = Math.min(point.x, Math.max(point1.x, viewport.getViewLocation().x));
        boolean flag = false;
        boolean flag1 = false;
        if (point2.x == point.x) point2.x -= i; else if (point2.x == point1.x) point2.x += i; else flag = true;
        if (dimension.height < rectangle.height) point2.y = Math.min(point1.y, Math.max(point.y, viewport.getViewLocation().y)); else point2.y = Math.min(point.y, Math.max(point1.y, viewport.getViewLocation().y));
        if (point2.y == point.y) point2.y -= j; else if (point2.y == point1.y) point2.y += j; else flag1 = true;
        if (flag ^ flag1) if (flag) point2.x = point.x - i; else point2.y = point.y - j;
        getFigureCanvas().scrollSmoothTo(point2.x, point2.y);
    }
}
