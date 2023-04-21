package org.openscience.cdk.renderer.progz;

import javax.vecmath.Point2d;
import org.openscience.cdk.renderer.ISimpleRenderer2D;

public interface IJava2DRenderer extends ISimpleRenderer2D {

    /**
	 * Returns model coordinates from screencoordinates provided by the graphics translation.
	 *   
	 * @return Point2D in real world coordinates
	 */
    public abstract Point2d getCoorFromScreen(int screenX, int screenY);
}
