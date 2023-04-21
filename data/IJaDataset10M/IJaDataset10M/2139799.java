package org.gvsig.symbology.fmap.labeling.placements;

import java.util.ArrayList;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.IPlacementConstraints;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.LabelClass;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.LabelLocationMetrics;
import com.iver.utiles.swing.threads.Cancellable;

/**
 *
 * MultiShapePlacementConstraints.java
 *
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es Apr 1, 2008
 *
 */
public class MultiShapePlacement implements ILabelPlacement {

    private ILabelPlacement pointPlacement;

    private ILabelPlacement linePlacement;

    private ILabelPlacement polygonPlacement;

    /**
	 * Creates a new instance of MultiShapePlacement initializing the respective
	 * placements to those passed as parameters. Null values are allowed for
	 * the parameters and will cause that no label will be placed when the
	 * geometry belongs to such null values.
	 *
	 * @param pointPlacement, the placement for points
	 * @param linePlacement, the placement for lines
	 * @param polygonPlacement, the placement for polygons
	 */
    public MultiShapePlacement(ILabelPlacement pointPlacement, ILabelPlacement linePlacement, ILabelPlacement polygonPlacement) {
        this.pointPlacement = pointPlacement;
        this.linePlacement = linePlacement;
        this.polygonPlacement = polygonPlacement;
    }

    public ArrayList<LabelLocationMetrics> guess(LabelClass lc, IGeometry geom, IPlacementConstraints placementConstraints, double cartographicSymbolSize, Cancellable cancel, ViewPort vp) {
        MultiShapePlacementConstraints pc = (MultiShapePlacementConstraints) placementConstraints;
        FShape shp = (FShape) geom.getInternalShape();
        switch(shp.getShapeType() % FShape.Z) {
            case FShape.POINT:
                if (pointPlacement != null) {
                    return pointPlacement.guess(lc, geom, pc.getPointConstraints(), cartographicSymbolSize, cancel, vp);
                }
                break;
            case FShape.LINE:
                if (linePlacement != null) {
                    return linePlacement.guess(lc, geom, pc.getLineConstraints(), cartographicSymbolSize, cancel, vp);
                }
                break;
            case FShape.POLYGON:
                if (polygonPlacement != null) {
                    return polygonPlacement.guess(lc, geom, pc.getPolygonConstraints(), cartographicSymbolSize, cancel, vp);
                }
                break;
        }
        return CannotPlaceLabel.NO_PLACES;
    }

    public boolean isSuitableFor(IPlacementConstraints placementConstraints, int shapeType) {
        return (shapeType % FShape.Z) == FShape.MULTI;
    }
}
