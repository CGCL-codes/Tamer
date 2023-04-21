package org.eclipse.smd.gef.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * La figure d'une decision.
 * @author Pierrick HYMBERT (phymbert [at] users.sourceforge.net) 
 */
public class SynchronisationStateFigure extends RoundedRectangle implements INamedFigure {

    public SynchronisationStateFigure(String name) {
        super();
        XYLayout layout = new XYLayout();
        setCornerDimensions(new Dimension(5, 5));
        setLayoutManager(layout);
        setBorder(null);
        setOpaque(true);
    }

    @Override
    protected void fillShape(Graphics graphics) {
        if (getForegroundColor() != null) graphics.setBackgroundColor(getForegroundColor());
        super.fillShape(graphics);
    }

    @Override
    protected void outlineShape(Graphics graphics) {
        if (getBackgroundColor() != null) graphics.setForegroundColor(getBackgroundColor());
        super.outlineShape(graphics);
    }

    public EditableLabel getNameLabel() {
        return null;
    }

    public void setName(String name) {
    }

    public void setSelected(boolean isSelected) {
    }
}
