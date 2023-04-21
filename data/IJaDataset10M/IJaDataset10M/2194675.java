package de.javacus.grafmach.threeD.sym;

import org.junit.Test;
import de.javacus.grafmach.twoD.GrafTestCase;

/**
 * Testet die Grafik eines runden Punktes auf einer Ecke des Wuerfels.
 * 
 * @author 2009 Burkhard Loesel, der Mensch stammt vom Affen ab
 * 
 */
public class PointOnCubeCornerTest extends GrafTestCase {

    @Test
    public void testPointOnCubeCornerPaint() {
        PointOnCubeCorner pointOnCubeCorner = new PointOnCubeCorner();
        pointOnCubeCorner.setSize(20);
        paintInJFrame(pointOnCubeCorner);
        pause(1);
    }

    @Test
    public void testPointOnCubeCornerSelected() {
        double xCenter = 100.0;
        double yCenter = 100.0;
        PointOnCubeCorner pointOnCubeCorner = new PointOnCubeCorner();
        pointOnCubeCorner.setXCenter(xCenter);
        pointOnCubeCorner.setYCenter(yCenter);
        pointOnCubeCorner.setSize(20);
        pointOnCubeCorner.setSelected(true);
        paintInJFrame(pointOnCubeCorner);
        assertEquals(true, ask("testPointOnCubeCornerPaint"));
        pause(1);
    }
}
