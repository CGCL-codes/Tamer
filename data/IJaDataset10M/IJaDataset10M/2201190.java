package jahuwaldt.plot;

import java.awt.*;

/**
*  <p> This class represents a triangle shaped plot
*      symbol pointing down.
*  </p>
*
*  <p>  Modified by:  Joseph A. Huwaldt  </p>
*
*  @author  Joseph A. Huwaldt   Date:  January 2, 2001
*  @version January 2, 2001
**/
public class Triangle3Symbol extends PolygonSymbol {

    private static final int kNumPoints = 3;

    /**
	*  Creates a triangle plot symbol pointing down
	*  that has a width of 8 pixels, is transparent and
	*  has a border color of black.
	**/
    public Triangle3Symbol() {
    }

    /**
	*  Method that determines the corner points of the
	*  polygon used to draw this plot symbol.  The corner
	*  points are stored in 2 arrays, xPoints and yPoints.
	*  If the arrays don't already exist for this symbol instance,
	*  this method must allocate them.
	*
	*  @param  x  The horizontal position of the center of the symbol.
	*  @param  y  The vertical position of the center of the symbol.
	**/
    protected void generatePoints(int x, int y) {
        if (xPoints == null) {
            xPoints = new int[kNumPoints];
            yPoints = new int[kNumPoints];
        }
        int width2 = getSize() / 2;
        int ymw2 = y - width2;
        xPoints[0] = x - width2;
        yPoints[0] = ymw2;
        xPoints[1] = x;
        yPoints[1] = y + width2;
        xPoints[2] = x + width2;
        yPoints[2] = ymw2;
    }
}
