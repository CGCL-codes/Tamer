package org.plazmaforge.studio.reportdesigner.layouts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

public class ReportDocumentLayout extends AbstractLayout {

    private final Map constraints = new HashMap();

    public Object getConstraint(final IFigure child) {
        return constraints.get(child);
    }

    public void layout(final IFigure container) {
        int pageWidth = 0;
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;
        for (final Iterator i = container.getChildren().iterator(); i.hasNext(); ) {
            final IFigure child = (IFigure) i.next();
            final Object constraint = getConstraint(child);
            if (constraint instanceof MarginConstraint) {
                final MarginConstraint marginConstraint = (MarginConstraint) constraint;
                switch(marginConstraint.getPosition()) {
                    case PositionConstants.LEFT:
                        left = marginConstraint.getSize().width;
                        break;
                    case PositionConstants.RIGHT:
                        right = marginConstraint.getSize().width;
                        break;
                    case PositionConstants.TOP:
                        top = marginConstraint.getSize().height;
                        pageWidth = marginConstraint.getSize().width;
                        break;
                    case PositionConstants.BOTTOM:
                        bottom = marginConstraint.getSize().height;
                        break;
                }
            }
        }
        final Rectangle clientArea = container.getClientArea();
        final int x = clientArea.x;
        int y = clientArea.y + top;
        for (Iterator i = container.getChildren().iterator(); i.hasNext(); ) {
            final IFigure child = (IFigure) i.next();
            final Object constraint = getConstraint(child);
            if (constraint instanceof Dimension) {
                final Dimension size = (Dimension) constraint;
                child.setBounds(new Rectangle(x + left, y, Math.max(size.width, pageWidth - (left + right)), size.height));
                y += size.height;
            } else if (constraint instanceof MarginConstraint) {
                child.setBounds(getMarginBounds(((MarginConstraint) constraint), clientArea, top, bottom, pageWidth));
            }
        }
    }

    public void remove(final IFigure child) {
        constraints.remove(child);
        super.remove(child);
    }

    public void setConstraint(final IFigure child, final Object constraint) {
        assert constraint == null || constraint instanceof Dimension || constraint instanceof MarginConstraint;
        constraints.put(child, constraint);
        super.setConstraint(child, constraint);
    }

    private Rectangle getMarginBounds(final MarginConstraint constraint, final Rectangle clientArea, int top, int bottom, int pageWidth) {
        switch(constraint.getPosition()) {
            case PositionConstants.LEFT:
                return new Rectangle(clientArea.x, clientArea.y + top, constraint.getSize().width, clientArea.height - (top + bottom));
            case PositionConstants.RIGHT:
                return new Rectangle(clientArea.x + pageWidth - constraint.getSize().width, clientArea.y + top, constraint.getSize().width, clientArea.height - (top + bottom));
            case PositionConstants.TOP:
                return new Rectangle(clientArea.getTopLeft(), constraint.getSize());
            case PositionConstants.BOTTOM:
                return new Rectangle(clientArea.x, clientArea.getBottom().y - bottom, pageWidth, bottom);
            default:
                throw new IllegalStateException();
        }
    }

    protected Dimension calculatePreferredSize(final IFigure container, int wHint, int hHint) {
        if (preferredSize == null) {
            int left = 0;
            int right = 0;
            int top = 0;
            int bottom = 0;
            preferredSize = new Dimension(0, 0);
            for (Iterator i = container.getChildren().iterator(); i.hasNext(); ) {
                final IFigure figure = (IFigure) i.next();
                final Object constraint = getConstraint(figure);
                if (constraint instanceof Dimension) {
                    final Dimension dimension = (Dimension) constraint;
                    preferredSize.height += dimension.height;
                    preferredSize.width = Math.max(preferredSize.width, dimension.width);
                } else {
                    final MarginConstraint marginConstraint = (MarginConstraint) constraint;
                    switch(marginConstraint.getPosition()) {
                        case PositionConstants.LEFT:
                            left = marginConstraint.getSize().width;
                            break;
                        case PositionConstants.RIGHT:
                            right = marginConstraint.getSize().width;
                            break;
                        case PositionConstants.TOP:
                            top = marginConstraint.getSize().height;
                            break;
                        case PositionConstants.BOTTOM:
                            bottom = marginConstraint.getSize().height;
                            break;
                        default:
                            assert false : marginConstraint.getPosition();
                    }
                }
            }
            preferredSize.expand(container.getInsets().getWidth() + left + right, container.getInsets().getHeight() + top + bottom);
        }
        return preferredSize;
    }
}
