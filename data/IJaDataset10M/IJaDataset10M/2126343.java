package rcsceneTests.junit.sceneInfo;

import sceneInfo.ObjectInfo;
import junit.framework.TestCase;

public class ObjectInfoTest extends TestCase {

    /** Test the constructors
	 * 
	 */
    public void testConstructor() {
        ObjectInfo oi = new ObjectInfo("myObject");
        assertTrue(oi.getType().equals("myObject"));
        assertEquals(oi.getDirection(), 0f);
        assertEquals(oi.getDistance(), 0f);
        assertEquals(oi.getDistChange(), 0f);
        assertEquals(oi.getDirChange(), 0f);
        assertEquals(oi.getTableRow(), -1);
        assertEquals(oi.getTableColumn(), -1);
    }

    /** Test getters and setters
	 * 
	 */
    public void testGettersAndSetters() {
        ObjectInfo oi = new ObjectInfo("myObj");
        oi.setType("newName");
        assertTrue(oi.getType().equals("newName"));
        oi.setDistance(5.8f);
        assertEquals(oi.getDistance(), 5.8f);
        oi.setDirection(9.4f);
        assertEquals(oi.getDirection(), 9.4f);
        oi.setDistChange(-0.1f);
        assertEquals(oi.getDistChange(), -0.1f);
        oi.setDirChange(4.2f);
        assertEquals(oi.getDirChange(), 4.2f);
        oi.setTableRow(3);
        assertEquals(oi.getTableRow(), 3);
        oi.setTableColumn(9);
        assertEquals(oi.getTableColumn(), 9);
    }
}
