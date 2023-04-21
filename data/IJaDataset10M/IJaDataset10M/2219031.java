package br.unb.unibiquitous.ubiquitos.app;

import org.junit.Assert;
import org.junit.Test;
import br.unb.unbiquitous.ubiquitos.listener.Coordinate;

public class TestCoordinate {

    @Test
    public void compareCoordinates() {
        Coordinate coordinate1 = new Coordinate();
        coordinate1.setX(100.0);
        coordinate1.setY(100.0);
        coordinate1.setZ(100.0);
        Coordinate coordinate2 = new Coordinate();
        coordinate2.setX(300.0);
        coordinate2.setY(300.0);
        coordinate2.setZ(300.0);
        boolean result = Coordinate.isInTheSamePlace(coordinate1, coordinate2);
        Assert.assertTrue(result);
        coordinate2.setX(600.0);
        coordinate2.setY(500.0);
        coordinate2.setZ(500.0);
        result = Coordinate.isInTheSamePlace(coordinate1, coordinate2);
        Assert.assertFalse(result);
        coordinate2.setX(500.0);
        coordinate2.setY(600.0);
        coordinate2.setZ(500.0);
        result = Coordinate.isInTheSamePlace(coordinate1, coordinate2);
        Assert.assertTrue(result);
        coordinate2.setX(500.0);
        coordinate2.setY(500.0);
        coordinate2.setZ(600.0);
        result = Coordinate.isInTheSamePlace(coordinate1, coordinate2);
        Assert.assertFalse(result);
        coordinate2.setX(-500.0);
        coordinate2.setY(500.0);
        coordinate2.setZ(-500.0);
        result = Coordinate.isInTheSamePlace(coordinate1, coordinate2);
        Assert.assertFalse(result);
        coordinate1 = new Coordinate(-11.727437973022461, -140.54286193847656, 665.4130859375);
        coordinate2 = new Coordinate(-13.119141578674316, -139.50729370117188, 664.7289428710938);
        result = Coordinate.isInTheSamePlace(coordinate1, coordinate2);
        Assert.assertTrue(result);
    }
}
