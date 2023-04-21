package fr.lip6.sma.simulacion.app.map.test;

import java.awt.Point;
import junit.framework.TestCase;
import fr.lip6.sma.simulacion.app.Configuration;
import fr.lip6.sma.simulacion.app.map.CellCoordinateConversion;

/**
 * Test class to validate the cell coordinate conversion methods.
 * 
 * @author Eric Platon <platon@nii.ac.jp>
 * @version $Revision: 3 $
 * 
 * @see "Nothing special"
 */
public class CellCoordinateConversionTest extends TestCase {

    /**
     * Useless constructor for this unit test class. Necessary for CS.
     */
    public CellCoordinateConversionTest() {
    }

    /**
     * Test the setup of the class by creating the object and calling a method.
     */
    public void testSetup() {
        final Configuration config = Configuration.getConfiguration("etc/simbar3.xml");
        final CellCoordinateConversion cellc = new CellCoordinateConversion(config);
        cellc.absolute2Cell(new Point(1, 1));
    }

    /**
     * Test the absolute to cell conversion method.
     * 
     * @see "etc/simbar3.xml for verifying the values"
     */
    public void testA2C() {
        final Configuration config = Configuration.getConfiguration("etc/simbar3.xml");
        final CellCoordinateConversion cellc = new CellCoordinateConversion(config);
        final Point onePoint = new Point(123, 345);
        final Point anotherPoint = cellc.absolute2Cell(onePoint);
        assertEquals(anotherPoint.x, 4);
        assertEquals(anotherPoint.y, 10);
    }

    /**
     * Test the cell to absolute conversion method.
     * 
     * @see "etc/simbar3.xml for verifying the values"
     */
    public void testC2A() {
        final Configuration config = Configuration.getConfiguration("etc/simbar3.xml");
        final CellCoordinateConversion cellc = new CellCoordinateConversion(config);
        final Point onePoint = new Point(2, 5);
        final Point anotherPoint = cellc.cell2Absolute(onePoint);
        assertEquals(anotherPoint.x, 66);
        assertEquals(anotherPoint.y, 165);
    }
}
