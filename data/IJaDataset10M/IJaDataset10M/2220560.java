package net.sf.redsetter.layout;

import net.sf.redsetter.part.MappingPart;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;

/**
 * Subclass of XYLayout which can use the child figures actual bounds as a constraint
 * when doing manual layout (XYLayout)
 */
public class GraphBorderLayout extends XYLayout {

    private MappingPart diagram;

    public GraphBorderLayout(MappingPart diagram) {
        this.diagram = diagram;
    }

    public void layout(IFigure container) {
        super.layout(container);
        diagram.setClassModelBounds();
    }
}
