package org.fudaa.fudaa.fgrid.action;

import gnu.trove.TIntHashSet;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.fudaa.ebli.calque.edition.ZCalquePointEditable;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.fudaa.fgrid.model.GeometricPointModel;
import com.vividsolutions.jts.geom.Coordinate;

public class DuplicatePointCleanerTest extends TestCase {

    private static final double eps = 0.1d;

    public void testDuplicatePointCleaner() {
        Coordinate pt1 = new Coordinate(3, 3);
        Coordinate pt2 = new Coordinate(3, 3);
        Coordinate pt3 = new Coordinate(3, 3);
        Coordinate pt4 = new Coordinate(8, 7);
        Coordinate pt5 = new Coordinate(13, 3);
        Coordinate pt6 = new Coordinate(13, 3);
        Coordinate pt7 = new Coordinate(18, 7);
        Coordinate pt8 = new Coordinate(24, 12);
        Coordinate pt9 = new Coordinate(24, 6);
        Coordinate pt10 = new Coordinate(29, 9);
        Coordinate pt11 = new Coordinate(29, 9);
        Coordinate pt12 = new Coordinate(30, 4);
        Coordinate pt13 = new Coordinate(15, 9);
        Coordinate pt14 = new Coordinate(15, 9);
        Coordinate pt15 = new Coordinate(20, 14);
        Coordinate pt16 = new Coordinate(18, 19);
        Coordinate pt17 = new Coordinate(12, 17);
        Coordinate pt18 = new Coordinate(10, 12);
        ContextForTesting context = new ContextForTesting();
        ZCalquePointEditable layer1 = context.getSupportPointLayer();
        ZCalquePointEditable layer2 = context.createPointsLayer();
        GeometricPointModel model1 = (GeometricPointModel) layer1.getModelEditable();
        GeometricPointModel model2 = (GeometricPointModel) layer2.getModelEditable();
        model1.addPointAndReturnId(new GrPoint(pt1), null, null);
        model1.addPointAndReturnId(new GrPoint(pt3), null, null);
        model1.addPointAndReturnId(new GrPoint(pt6), null, null);
        model1.addPointAndReturnId(new GrPoint(pt10), null, null);
        model1.addPointAndReturnId(new GrPoint(pt11), null, null);
        model1.addPointAndReturnId(new GrPoint(pt14), null, null);
        model1.addPointAndReturnId(new GrPoint(pt15), null, null);
        model1.addPointAndReturnId(new GrPoint(pt17), null, null);
        model1.addPointAndReturnId(new GrPoint(pt18), null, null);
        model2.addPointAndReturnId(new GrPoint(pt2), null, null);
        model2.addPointAndReturnId(new GrPoint(pt4), null, null);
        model2.addPointAndReturnId(new GrPoint(pt5), null, null);
        model2.addPointAndReturnId(new GrPoint(pt7), null, null);
        model2.addPointAndReturnId(new GrPoint(pt8), null, null);
        model2.addPointAndReturnId(new GrPoint(pt9), null, null);
        model2.addPointAndReturnId(new GrPoint(pt12), null, null);
        model2.addPointAndReturnId(new GrPoint(pt13), null, null);
        model2.addPointAndReturnId(new GrPoint(pt16), null, null);
        List<ZCalquePointEditable> layers = new ArrayList<ZCalquePointEditable>();
        layers.add(layer1);
        layers.add(layer2);
        DuplicatePointCleaner cleaner = new DuplicatePointCleaner(layers, null);
        cleaner.process();
        assertEquals("Pas le bon nombre de points supprim�s", 5, cleaner.getNbPointsCleaned());
        Coordinate[] uniquePoints = new Coordinate[] { new Coordinate(10, 12), new Coordinate(12, 17), new Coordinate(13, 3), new Coordinate(15, 9), new Coordinate(18, 19), new Coordinate(18, 7), new Coordinate(20, 14), new Coordinate(24, 12), new Coordinate(24, 6), new Coordinate(29, 9), new Coordinate(3, 3), new Coordinate(30, 4), new Coordinate(8, 7) };
        Coordinate[] points = this.getAllPoints(layers);
        assertEquals("Pas le bon nombre de points", uniquePoints.length, points.length);
        assertTrue("Les points ne sont pas les m�me", this.isSamePoints(uniquePoints, points));
    }

    private boolean isSamePoints(Coordinate[] points1, Coordinate[] points2) {
        if (points1.length != points2.length) {
            return false;
        }
        TIntHashSet findedPoints = new TIntHashSet();
        for (int i = 0; i < points1.length; i++) {
            for (int j = 0; j < points2.length; j++) {
                if (!findedPoints.contains(j)) {
                    if (points1[i].distance(points2[j]) <= eps) {
                        findedPoints.add(j);
                        break;
                    }
                }
            }
        }
        return findedPoints.size() == points1.length;
    }

    private Coordinate[] getAllPoints(List<ZCalquePointEditable> layers) {
        List<Coordinate> listPoints = new ArrayList<Coordinate>();
        for (int i = 0; i < layers.size(); i++) {
            GeometricPointModel model = (GeometricPointModel) layers.get(i).getModelEditable();
            int nbPoints = model.getNombre();
            for (int j = 0; j < nbPoints; j++) {
                listPoints.add(new Coordinate(model.getX(j), model.getY(j)));
            }
        }
        Coordinate[] points = new Coordinate[0];
        points = listPoints.toArray(points);
        return points;
    }
}
