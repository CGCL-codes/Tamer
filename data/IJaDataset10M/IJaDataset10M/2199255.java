package com.vividsolutions.jump.workbench.ui.cursortool;

import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.geom.EnvelopeIntersector;
import com.vividsolutions.jump.workbench.model.Layer;

/**
 * Allows the user to specify features by clicking on them or drawing a box
 * around them. Works with invalid features (using EnvelopeIntersector).
 */
public abstract class SpecifyFeaturesTool extends DragTool {

    public SpecifyFeaturesTool() {
    }

    protected Iterator candidateLayersIterator() {
        return getPanel().getLayerManager().iterator();
    }

    /**
	 * @param envelope
	 *            the envelope, which may have zero area
	 * @return those features of the layer that intersect the given envelope; an
	 *         empty FeatureCollection if no features intersect it
	 */
    private static Set intersectingFeatures(Layer layer, Envelope envelope) {
        HashSet intersectingFeatures = new HashSet();
        List candidateFeatures = layer.getFeatureCollectionWrapper().query(envelope);
        String a = "" + layer.getFeatureCollectionWrapper().getUltimateWrappee();
        String b = "" + layer.getFeatureCollectionWrapper().getUltimateWrappee().size();
        for (Iterator i = candidateFeatures.iterator(); i.hasNext(); ) {
            Feature feature = (Feature) i.next();
            if (envelope.contains(feature.getGeometry().getEnvelopeInternal()) || EnvelopeIntersector.intersects(feature.getGeometry(), envelope)) {
                intersectingFeatures.add(feature);
            }
        }
        return intersectingFeatures;
    }

    public void mouseClicked(MouseEvent e) {
        try {
            super.mouseClicked(e);
            setViewSource(e.getPoint());
            setViewDestination(e.getPoint());
            fireGestureFinished();
        } catch (Throwable t) {
            getPanel().getContext().handleThrowable(t);
        }
    }

    protected Set specifiedFeatures() throws NoninvertibleTransformException {
        HashSet allFeatures = new HashSet();
        for (Iterator i = layerToSpecifiedFeaturesMap().values().iterator(); i.hasNext(); ) {
            Set features = (Set) i.next();
            allFeatures.addAll(features);
        }
        return allFeatures;
    }

    /**
	 * Returns the layers containing the specified features, and the specified
	 * features themselves.
	 */
    protected Map layerToSpecifiedFeaturesMap() throws NoninvertibleTransformException {
        return layerToSpecifiedFeaturesMap(candidateLayersIterator(), getBoxInModelCoordinates());
    }

    public static Map layerToSpecifiedFeaturesMap(Iterator layerIterator, Envelope boxInModelCoordinates) throws NoninvertibleTransformException {
        HashMap layerToFeaturesMap = new HashMap();
        for (Iterator i = layerIterator; i.hasNext(); ) {
            Layer layer = (Layer) i.next();
            if (!layer.isVisible()) {
                continue;
            }
            Set intersectingFeatures = intersectingFeatures(layer, boxInModelCoordinates);
            if (intersectingFeatures.isEmpty()) {
                continue;
            }
            layerToFeaturesMap.put(layer, intersectingFeatures);
        }
        return layerToFeaturesMap;
    }

    /**
	 * @param layers
	 *            Layers to filter in
	 */
    protected Collection specifiedFeatures(Collection layers) throws NoninvertibleTransformException {
        ArrayList specifiedFeatures = new ArrayList();
        Map layerToSpecifiedFeaturesMap = layerToSpecifiedFeaturesMap();
        for (Iterator i = layerToSpecifiedFeaturesMap.keySet().iterator(); i.hasNext(); ) {
            Layer layer = (Layer) i.next();
            if (!layers.contains(layer)) {
                continue;
            }
            specifiedFeatures.addAll((Collection) layerToSpecifiedFeaturesMap.get(layer));
        }
        return specifiedFeatures;
    }
}
