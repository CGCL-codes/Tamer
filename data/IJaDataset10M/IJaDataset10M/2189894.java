package fd.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

public class FeatureRelationCaseOrNodeFigure extends FeatureRelationCaseNodeFigure {

    protected void outlineShape(Graphics graphics) {
        super.outlineShape(graphics);
        graphics.setBackgroundColor(graphics.getForegroundColor());
        this.fillShape(graphics);
    }
}
