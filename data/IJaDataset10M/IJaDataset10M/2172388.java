package wood.model;

import java.util.Iterator;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wood.model.map.Map;
import wood.model.map.HexMapArray;
import wood.model.map.Map.Tile;

public class TestMap extends TestCase {

    private Map map = new Map(4, 4);

    private HexMapArray<Tile> mapArr = new HexMapArray<Tile>(4, 4);

    public static void main(String args[]) {
        junit.textui.TestRunner.run(TestMap.class);
    }

    @Before
    protected void setup() throws Exception {
        super.setUp();
    }

    @Override
    @After
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testMapIsValidMethod() {
        for (int i = 0; i < mapArr.getWidth(); ++i) {
            for (int j = 0; j < mapArr.getHeight(); ++j) {
                mapArr.set(i, j, map.new Tile(i, j));
            }
        }
        Iterator<Tile> i = mapArr.getIterable().iterator();
        while (i.hasNext()) {
            if (i.next() == null) fail();
        }
        Assert.assertTrue(true);
    }

    public void testMapFixesCoordinates() {
        for (int i = 0; i < mapArr.getWidth(); ++i) {
            for (int j = 0; j < mapArr.getMapSize(); ++j) {
                mapArr.set(i, j, map.new Tile(i, j));
                double x = mapArr.get(i, j).getPointLocation().getX();
                double y = mapArr.get(i, j).getPointLocation().getY();
                System.out.println("Point: " + x + ", " + y);
            }
        }
        Iterator<Tile> i = mapArr.getIterable().iterator();
        while (i.hasNext()) {
            Assert.assertNotNull(i.next());
        }
    }
}
