package org.plazmaforge.studio.reportdesigner.handles;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.AbstractHandle;
import org.plazmaforge.studio.reportdesigner.tools.VerticalResizeTracker;

public class VerticalResizeHandle extends AbstractHandle {

    private static final class RowBottomLocator implements Locator {

        private final IFigure reference;

        private final IFigure leftBoundSource;

        public RowBottomLocator(final IFigure reference, final IFigure leftBoundSource) {
            this.reference = reference;
            this.leftBoundSource = leftBoundSource;
        }

        public void relocate(final IFigure target) {
            final Rectangle bounds = reference.getBounds().getCopy();
            bounds.y = bounds.getBottom().y - 1;
            bounds.height = 2;
            reference.translateToAbsolute(bounds);
            final Rectangle rectangle = leftBoundSource.getBounds().getCopy();
            leftBoundSource.translateToAbsolute(rectangle);
            rectangle.y = bounds.y;
            rectangle.height = bounds.height;
            rectangle.width = 1;
            bounds.union(rectangle);
            target.translateToRelative(bounds);
            target.setBounds(bounds);
        }
    }

    public VerticalResizeHandle(final GraphicalEditPart editPart, final GraphicalEditPart leftEditPart) {
        this(editPart, new RowBottomLocator(editPart.getFigure(), leftEditPart.getFigure()));
    }

    public VerticalResizeHandle(final GraphicalEditPart editPart, final Locator locator) {
        super(editPart, locator, Cursors.SIZENS);
    }

    protected DragTracker createDragTracker() {
        return new VerticalResizeTracker(getOwner());
    }

    public void paint(Graphics graphics) {
    }
}
