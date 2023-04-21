package org.gvsig.symbology.fmap.labeling.placements;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.core.FPoint2D;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.IPlacementConstraints;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.LabelClass;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.LabelLocationMetrics;
import com.iver.utiles.swing.threads.Cancellable;

public class MarkerPlacementOnPoint implements ILabelPlacement {

    public ArrayList<LabelLocationMetrics> guess(LabelClass lc, FShape shp, IPlacementConstraints placementConstraints, double cartographicSymbolSize, Cancellable cancel) {
        if (cancel.isCanceled()) return CannotPlaceLabel.NO_PLACES;
        ArrayList<LabelLocationMetrics> guessed = new ArrayList<LabelLocationMetrics>();
        FPoint2D fp = (FPoint2D) shp;
        Point2D p = new Point2D.Double(fp.getX(), fp.getY());
        Rectangle2D bounds = lc.getBounds();
        p.setLocation(p.getX() - (bounds.getWidth() * 0.5), p.getY() - (bounds.getHeight() * 0.5));
        guessed.add(new LabelLocationMetrics(p, 0, true));
        return guessed;
    }

    public ArrayList<LabelLocationMetrics> guess(LabelClass lc, IGeometry geom, IPlacementConstraints placementConstraints, double cartographicSymbolSize, Cancellable cancel, ViewPort vp) {
        if (cancel.isCanceled()) return CannotPlaceLabel.NO_PLACES;
        FShape shp = FConverter.transformToInts(geom, vp.getAffineTransform());
        ArrayList<LabelLocationMetrics> guessed = new ArrayList<LabelLocationMetrics>();
        FPoint2D fp = (FPoint2D) shp;
        Point2D p = new Point2D.Double(fp.getX(), fp.getY());
        Rectangle2D bounds = lc.getBounds();
        p.setLocation(p.getX() - (bounds.getWidth() * 0.5), p.getY() - bounds.getHeight() * 0.5);
        guessed.add(new LabelLocationMetrics(p, 0, true));
        return guessed;
    }

    public boolean isSuitableFor(IPlacementConstraints placementConstraints, int shapeType) {
        if ((shapeType % FShape.Z) == FShape.POINT) {
            return placementConstraints.isOnTopOfThePoint();
        }
        return false;
    }
}
