package net.sf.isnake.core;

import java.util.ArrayList;
import java.util.List;
import junit.framework.*;

/**
 *
 * @author  Abhishek Dutta (adutta.np@gmail.com)
 * @version $Id: MiscTest.java 87 2008-02-21 11:32:07Z thelinuxmaniac $
 */
public class MiscTest extends TestCase {

    public MiscTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testArrayList() {
        List<GameFieldCoordinate> myList = new ArrayList<GameFieldCoordinate>();
        GameFieldCoordinate gfc = new GameFieldCoordinate(20, 20);
        myList.add(gfc);
        System.out.println("Old: " + myList);
        gfc.setY(gfc.getY() + 10);
        System.out.println("New: " + myList);
        System.out.println("Integer's max value = " + Integer.MAX_VALUE);
        System.out.println("Long's max value = " + Long.MAX_VALUE);
        GameFieldCoordinate g1 = new GameFieldCoordinate(10, 10);
        GameFieldCoordinate g2 = new GameFieldCoordinate(10, 10);
        GameFieldCoordinate g3 = new GameFieldCoordinate(11, 10);
        System.out.println("Equals : " + g1.equals(g2));
        System.out.println("Not Equals : " + g1.equals(g3));
        GameFieldCoordinate gc = new GameFieldCoordinate(10, 10);
        System.out.println("old val=" + gc);
        gc.move((byte) (-1), (byte) 0);
        System.out.println("new val=" + gc);
    }
}
