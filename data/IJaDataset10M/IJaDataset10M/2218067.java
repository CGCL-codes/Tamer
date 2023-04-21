package utils.pointfield.attractor;

import utils.pointfield.Quadtree;

/**
 *
 * @author gtg126z
 */
public abstract class AttractorFunction2 extends AttractorFunction<APoint2d> {

    public APoint2d getNewPoint() {
        return new APoint2d();
    }

    public Quadtree makeQuadtree() {
        return new QT2d();
    }
}
