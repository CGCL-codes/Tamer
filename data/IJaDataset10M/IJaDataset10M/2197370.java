package openome_model.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.PointList;

public class AndDecoration extends PolylineDecoration {

    public AndDecoration() {
        setFill(true);
        setFillXOR(false);
        setOutline(true);
        setOutlineXOR(false);
        setLineWidth(3);
        setLineStyle(Graphics.LINE_SOLID);
        PointList template = new PointList();
        int midpoint = -3;
        int lengthOfCrossLine = 3;
        int topPoint = midpoint - lengthOfCrossLine;
        int bottomPoint = midpoint + lengthOfCrossLine;
        int xDisplacement = 4;
        template.addPoint(getStart().getTranslated(xDisplacement, topPoint));
        template.addPoint(getStart().getTranslated(xDisplacement, bottomPoint));
        setTemplate(template);
    }
}
