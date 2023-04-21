package repast.simphony.agents.designer.figures;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif�s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * TODO
 * 
 * 
 * 
 */
public class PropertyOrStepConnectionAnchor extends AbstractConnectionAnchor {

    private int xOffset;

    private int yOffset;

    /**
	 * TODO
	 * 
	 */
    public PropertyOrStepConnectionAnchor() {
        super();
    }

    /**
	 * TODO
	 * 
	 * @param owner
	 */
    public PropertyOrStepConnectionAnchor(IFigure owner) {
        super(owner);
    }

    /**
	 * TODO
	 * 
	 * @param offset
	 */
    public void setOffset(int x, int y) {
        xOffset = x;
        yOffset = y;
    }

    /**
	 * @see org.eclipse.draw2d.ConnectionAnchor#getLocation(org.eclipse.draw2d.geometry.Point)
	 */
    public Point getLocation(Point reference) {
        Rectangle r = getOwner().getBounds();
        int x, y;
        x = r.x + xOffset;
        y = r.y + yOffset;
        Point p = new PrecisionPoint(x, y);
        getOwner().translateToAbsolute(p);
        return p;
    }

    /**
	 * @see org.eclipse.draw2d.ConnectionAnchor#getReferencePoint()
	 */
    @Override
    public Point getReferencePoint() {
        return getLocation(null);
    }
}
