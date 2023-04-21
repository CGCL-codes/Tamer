package jahuwaldt.plot;

import java.awt.*;

/**
*  <p> This class represents a tab pointing up (house)
*      plot symbol.
*  </p>
*
*  <p>  Modified by:  Joseph A. Huwaldt  </p>
*
*  @author  Joseph A. Huwaldt   Date:  January 2, 2001
*  @version January 2, 2001
**/
public class TabUpSymbol extends PolygonSymbol {

    private static final int kNumPoints = 5;

    /**
	*  Creates a tab pointing up (house) plot
	*  symbol that has a width of 8 pixels, is transparent and
	*  has a border color of black.
	**/
    public TabUpSymbol() {
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
        int xmw2 = x - width2;
        int xpw2 = x + width2;
        int ypw2 = y + width2;
        xPoints[0] = x;
        yPoints[0] = y - width2;
        xPoints[1] = xmw2;
        yPoints[1] = y;
        xPoints[2] = xmw2;
        yPoints[2] = ypw2;
        xPoints[3] = xpw2;
        yPoints[3] = ypw2;
        xPoints[4] = xpw2;
        yPoints[4] = y;
    }
}
