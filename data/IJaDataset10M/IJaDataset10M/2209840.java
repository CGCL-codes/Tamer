package org.jhotdraw.figures;

import org.jhotdraw.util.Storable;
import java.awt.*;
import java.io.Serializable;

/**
 * Decorate the start or end point of a line or poly line figure.
 * LineDecoration is the base class for the different line decorations.
 *
 * @see PolyLineFigure
 *
 * @version <$CURRENT_VERSION$>
 */
public interface LineDecoration extends Storable, Cloneable, Serializable {

    /**
	 * Draws the decoration in the direction specified by the two points.
	 */
    public void draw(Graphics g, int x1, int y1, int x2, int y2);

    /**
	 * @return the display box of a LineDecoration.
	 */
    public Rectangle displayBox();
}
