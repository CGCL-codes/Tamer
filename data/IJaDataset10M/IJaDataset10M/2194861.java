package net.sourceforge.copernicus.client.view;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

public class InitialConstraintSelector extends ConstraintSelector {

    public InitialConstraintSelector(IFigure figure) {
        super(figure);
    }

    @Override
    public Rectangle newChild(IFigure child) {
        int x = GAP;
        for (Object ochild : figure.getChildren()) {
            IFigure fchild = (IFigure) ochild;
            x += fchild.getPreferredSize().width + GAP;
        }
        return new Rectangle(x, GAP, -1, -1);
    }
}
