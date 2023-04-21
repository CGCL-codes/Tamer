package piramide.interaction.reasoner.creator.membershipgenerator.regionaccumulator;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Test;
import piramide.interaction.reasoner.creator.Point;
import piramide.interaction.reasoner.creator.membershipgenerator.regionaccumulator.Subregion;
import static org.junit.Assert.*;

@SuppressWarnings("boxing")
public class SubregionTest {

    @Test
    public void testCalculateDescendingPoints() {
        final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
        values2trends.put(0.0, 2.0);
        values2trends.put(1.0, 2.0);
        values2trends.put(2.0, 2.0);
        values2trends.put(3.0, 2.0);
        final Subregion sr = new Subregion(values2trends);
        final List<Point> points = sr.calculateDescendingPoints();
        assertEquals(4, points.size());
        assertEquals(0.0, points.get(0).getValue(), 0.01);
        assertEquals(1.0, points.get(0).getTrend(), 0.01);
        assertEquals(1.0, points.get(1).getValue(), 0.01);
        assertEquals(2.0 / 3, points.get(1).getTrend(), 0.01);
        assertEquals(2.0, points.get(2).getValue(), 0.01);
        assertEquals(1.0 / 3, points.get(2).getTrend(), 0.01);
        assertEquals(3.0, points.get(3).getValue(), 0.01);
        assertEquals(0.0, points.get(3).getTrend(), 0.01);
    }

    @Test
    public void testCalculateAscendingPoints() {
        final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
        values2trends.put(0.0, 2.0);
        values2trends.put(1.0, 2.0);
        values2trends.put(2.0, 2.0);
        values2trends.put(3.0, 2.0);
        final Subregion sr = new Subregion(values2trends);
        final List<Point> points = sr.calculateAscendingPoints();
        assertEquals(4, points.size());
        assertEquals(0.0, points.get(0).getValue(), 0.01);
        assertEquals(0.0, points.get(0).getTrend(), 0.01);
        assertEquals(1.0, points.get(1).getValue(), 0.01);
        assertEquals(1.0 / 3, points.get(1).getTrend(), 0.01);
        assertEquals(2.0, points.get(2).getValue(), 0.01);
        assertEquals(2.0 / 3, points.get(2).getTrend(), 0.01);
        assertEquals(3.0, points.get(3).getValue(), 0.01);
        assertEquals(1.0, points.get(3).getTrend(), 0.01);
    }

    @Test
    public void testCalculateAscendingPoints2() {
        final Map<Number, Double> values2trends = new TreeMap<Number, Double>();
        values2trends.put(0.0, 10.0);
        values2trends.put(6.0, 27.0);
        values2trends.put(8.0, 12.0);
        values2trends.put(10.0, 99.0);
        final Subregion sr = new Subregion(values2trends);
        final List<Point> points = sr.calculateAscendingPoints();
        assertEquals(4, points.size());
        assertEquals(0.0, points.get(0).getValue(), 0.01);
        assertEquals(0.0, points.get(0).getTrend(), 0.00001);
        assertEquals(6.0, points.get(1).getValue(), 0.01);
        assertEquals(26.2 / 148, points.get(1).getTrend(), 0.00001);
        assertEquals(8.0, points.get(2).getValue(), 0.01);
        assertEquals(46.6 / 148, points.get(2).getTrend(), 0.00001);
        assertEquals(10.0, points.get(3).getValue(), 0.01);
        assertEquals(1.0, points.get(3).getTrend(), 0.00001);
    }
}
