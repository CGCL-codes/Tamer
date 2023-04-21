package com.vividsolutions.jts.awt;

import java.awt.geom.Point2D;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Copies point ordinates with no transformation.
 * 
 * @author Martin Davis
 *
 */
public class IdentityPointTransformation implements PointTransformation {

    @Override
    public void transform(Coordinate model, Point2D view) {
        view.setLocation(model.x, model.y);
    }
}
