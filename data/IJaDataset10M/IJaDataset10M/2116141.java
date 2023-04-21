package org.gvsig.gpe.writer;

import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.GeometryAsserts;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.MultiPoint;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public abstract class GPEMultiPointLayerTest extends GPEWriterBaseTest {

    private String layerId = "l1";

    private String srs = "EPSG:23030";

    private String feature1Id = "f1";

    private String multiPoint1Id = "mp1";

    private String point1Id = "p1";

    private double point1X = generateRandomPoint();

    private double point1Y = generateRandomPoint();

    private double point1Z = generateRandomPoint();

    private String point2Id = "p2";

    private double point2X = generateRandomPoint();

    private double point2Y = generateRandomPoint();

    private double point2Z = generateRandomPoint();

    private String point3Id = "p3";

    private double point3X = generateRandomPoint();

    private double point3Y = generateRandomPoint();

    private double point3Z = generateRandomPoint();

    public void readObjects() {
        Layer[] layers = getLayers();
        assertEquals(layers.length, 1);
        Layer layer = layers[0];
        assertEquals(layer.getFeatures().size(), 1);
        Feature feature1 = (Feature) layer.getFeatures().get(0);
        MultiPoint multiPoint = (MultiPoint) feature1.getGeometry();
        assertEquals(multiPoint.getGeometries().size(), 3);
        GeometryAsserts.point(multiPoint.getMultiPointAt(0), point1X, point1Y, point1Z);
        GeometryAsserts.point(multiPoint.getMultiPointAt(1), point2X, point2Y, point2Z);
        GeometryAsserts.point(multiPoint.getMultiPointAt(2), point3X, point3Y, point3Z);
    }

    public void writeObjects() {
        getWriterHandler().initialize();
        getWriterHandler().startLayer(layerId, null, null, srs, null);
        getWriterHandler().startFeature(feature1Id, null, null);
        getWriterHandler().startMultiPoint(multiPoint1Id, srs);
        getWriterHandler().startPoint(point1Id, new CoordinatesSequence(point1X, point1Y, point1Z), srs);
        getWriterHandler().endPoint();
        getWriterHandler().startPoint(point2Id, new CoordinatesSequence(point2X, point2Y, point2Z), srs);
        getWriterHandler().endPoint();
        getWriterHandler().startPoint(point3Id, new CoordinatesSequence(point3X, point3Y, point3Z), srs);
        getWriterHandler().endPoint();
        getWriterHandler().endMultiPoint();
        getWriterHandler().endFeature();
        getWriterHandler().endLayer();
        getWriterHandler().close();
    }
}
