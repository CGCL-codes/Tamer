package org.gvsig.jts.voronoi;

import java.awt.geom.Point2D;
import org.gvsig.exceptions.BaseException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

/**
 * Vectorial layer whose geometries can be triangulated (TIN) or can 
 * participate in a voronoi diagram.
 * 
 * @author Alvaro Zabala
 *
 */
public abstract class VoronoiAndTinInputLyr extends FLyrVect {

    public interface VertexVisitor {

        public void visit(Point2D vertex);
    }

    public abstract Point2D getPoint(int geometryIndex);

    public void visitVertices(VertexVisitor visitor, boolean onlySelection) throws BaseException {
        int shapeCount = getSource().getShapeCount();
        for (int i = 0; i < shapeCount; i++) {
            if (onlySelection) {
                if (!getRecordset().getSelection().get(i)) continue;
            }
            Point2D point = getPoint(i);
            visitor.visit(point);
        }
    }
}
