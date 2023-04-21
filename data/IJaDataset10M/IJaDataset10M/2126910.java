package com.vividsolutions.jump.workbench.ui.cursortool;

import java.awt.Color;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Icon;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.CollectionUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;

public class SplitLineStringTool extends AbstractClickSelectedLineStringsTool {

    protected void gestureFinished(Collection nearbyLineStringFeatures) throws NoninvertibleTransformException {
        Feature closestFeature = closest(nearbyLineStringFeatures, getModelClickPoint());
        LineString lineString = (LineString) closestFeature.getGeometry();
        if (CollectionUtil.list(lineString.getStartPoint().getCoordinate(), lineString.getEndPoint().getCoordinate()).contains(DistanceOp.closestPoints(lineString, getModelClickPoint())[0])) {
            getWorkbench().getFrame().warnUser(NO_SELECTED_LINESTRINGS_HERE_MESSAGE);
            return;
        }
        if (!layer(closestFeature, layerToSpecifiedFeaturesMap()).isEditable()) {
            warnLayerNotEditable(layer(closestFeature, layerToSpecifiedFeaturesMap()));
            return;
        }
        split(closestFeature, getModelDestination(), layer(closestFeature, layerToSpecifiedFeaturesMap()));
    }

    private void split(Feature feature, Coordinate coordinate, Layer layer) {
        new SplitLineStringsOp(Color.blue).addSplit(feature, coordinate, layer, false).execute(getName(), isRollingBackInvalidEdits(), getPanel());
    }

    private Feature closest(Collection features, Point point) {
        Feature closestFeature = null;
        double closestDistance = Double.MAX_VALUE;
        for (Iterator i = features.iterator(); i.hasNext(); ) {
            Feature feature = (Feature) i.next();
            double distance = feature.getGeometry().distance(point);
            if (distance < closestDistance) {
                closestFeature = feature;
                closestDistance = distance;
            }
        }
        return closestFeature;
    }

    public Icon getIcon() {
        return IconLoader.icon("SplitLinestring.gif");
    }
}
