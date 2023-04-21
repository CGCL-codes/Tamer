package com.vividsolutions.jump.workbench.ui.plugin.test;

import java.util.ArrayList;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.geom.CoordUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.renderer.style.ArrowLineStringEndpointStyle;

public class RandomArrowsPlugIn extends AbstractPlugIn {

    private static final int FEATURE_COUNT = 20;

    private static final double LAYER_SIDE_LENGTH = 100;

    private static final int MAX_SEGMENT_COUNT = 3;

    private static final double MAX_SEGMENT_LENGTH = 20;

    private GeometryFactory geometryFactory = new GeometryFactory();

    public RandomArrowsPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addLayerViewMenuItem(this, new String[] { "Tools", "Generate" }, getName());
    }

    public boolean execute(PlugInContext context) throws Exception {
        FeatureSchema schema = new FeatureSchema();
        schema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
        FeatureDataset dataset = new FeatureDataset(schema);
        for (int i = 0; i < FEATURE_COUNT; i++) {
            dataset.add(createFeature(schema));
        }
        addLayer(dataset, context);
        return true;
    }

    private void addLayer(FeatureCollection featureCollection, PlugInContext context) {
        Layer layer = new Layer("Random Arrows", context.getLayerManager().generateLayerFillColor(), featureCollection, context.getLayerManager());
        boolean firingEvents = context.getLayerManager().isFiringEvents();
        context.getLayerManager().setFiringEvents(false);
        try {
            layer.addStyle(new ArrowLineStringEndpointStyle.NarrowSolidEnd());
        } finally {
            context.getLayerManager().setFiringEvents(firingEvents);
        }
        context.getLayerManager().addLayer(StandardCategoryNames.WORKING, layer);
    }

    private Feature createFeature(FeatureSchema schema) {
        ArrayList coordinates = new ArrayList();
        coordinates.add(CoordUtil.add(new Coordinate(LAYER_SIDE_LENGTH / 2d, LAYER_SIDE_LENGTH / 2d), randomCoordinate(LAYER_SIDE_LENGTH / 2d)));
        int walkMax = (int) Math.ceil(Math.random() * MAX_SEGMENT_LENGTH);
        int segmentCount = (int) Math.ceil(Math.random() * MAX_SEGMENT_COUNT);
        for (int i = 0; i < segmentCount; i++) {
            Coordinate prevCoordinate = (Coordinate) coordinates.get(coordinates.size() - 1);
            coordinates.add(CoordUtil.add(prevCoordinate, randomCoordinate(walkMax)));
        }
        LineString lineString = geometryFactory.createLineString((Coordinate[]) coordinates.toArray(new Coordinate[] {}));
        Feature feature = new BasicFeature(schema);
        feature.setGeometry(lineString);
        return feature;
    }

    private Coordinate randomCoordinate(double walkMax) {
        return CoordUtil.add(new Coordinate(-walkMax / 2d, -walkMax / 2d), new Coordinate(Math.random() * walkMax, Math.random() * walkMax));
    }
}
