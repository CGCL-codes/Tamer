package com.mapquest.spatialbase;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.mapquest.spatialbase.CartesianPoint;
import com.mapquest.spatialbase.FixedLatLng;
import com.mapquest.spatialbase.LatLng;

public class LatLngTest {

    @Test
    public void testLlToCartesianAndBack() {
        CartesianPoint pt = new FixedLatLng(-90000000, -180000000).toCartesian();
        assertEquals(0, pt.getX());
        assertEquals(0, pt.getY());
        LatLng inverse = LatLng.fromCartesian(pt);
        assertEquals(-90000000, inverse.getFixedLat());
        assertEquals(-180000000, inverse.getFixedLng());
        pt = new FixedLatLng(+90000000, +180000000).toCartesian();
        assertEquals(360000000, pt.getX());
        assertEquals(180000000, pt.getY());
        inverse = LatLng.fromCartesian(pt);
        assertEquals(+90000000, inverse.getFixedLat());
        assertEquals(+180000000, inverse.getFixedLng());
        pt = new FixedLatLng(+90000000, +181000000).toCartesian();
        assertEquals(1000000, pt.getX());
        assertEquals(180000000, pt.getY());
        inverse = LatLng.fromCartesian(pt);
        assertEquals(+90000000, inverse.getFixedLat());
        assertEquals(-179000000, inverse.getFixedLng());
    }
}
