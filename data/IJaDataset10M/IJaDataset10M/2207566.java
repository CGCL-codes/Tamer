package org.skycastle.core.old.geometry.space.grid;

import junit.framework.TestCase;
import org.skycastle.core.old.GameContext;
import org.skycastle.core.old.GameObject;
import org.skycastle.core.old.UnitTestingContext;
import org.skycastle.core.old.pojo.PojoGameObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans Haggstrom
 */
public class GridTest extends TestCase {

    private GridSpace myGridSpace;

    private static final TileType TEST_TILE_TYPE = new TileType() {

        public String getTileTypeIdentifier() {
            return null;
        }

        public String getGroundTexture() {
            return null;
        }
    };

    public void testAddingAndAccessingGameObjects() throws Exception {
        assertObjectAccessWorks(true, 5.5f, 4.5f, 5, 4);
        assertObjectAccessWorks(true, 5.0f, 4.0f, 5, 4);
        assertObjectAccessWorks(true, 0.5f, 0.5f, 0, 0);
        assertObjectAccessWorks(true, 9.5f, 9.5f, 9, 9);
        assertObjectAccessWorks(false, 6.5f, 4.5f, 5, 4);
        assertObjectAccessWorks(false, 6.5f, 5.5f, 5, 4);
        assertObjectAccessWorks(false, 6.0f, 4.5f, 5, 4);
    }

    public void testTileTypeSettingAndGetting() throws Exception {
        assertNull(myGridSpace.getTileType(4, 5));
        myGridSpace.setTileType(4, 5, TEST_TILE_TYPE);
        assertEquals(TEST_TILE_TYPE, myGridSpace.getTileType(4, 5));
    }

    @Override
    protected void setUp() throws Exception {
        GameContext.setGameObjectContextOnClient(new UnitTestingContext());
        myGridSpace = new GridSpace(10, 10);
    }

    private void assertObjectAccessWorks(boolean shouldContain, final float x, final float y, final int xGrid, final int yGrid) {
        final PojoGameObject testObject = new PojoGameObject() {
        };
        myGridSpace.addObject(testObject, x, y, 0);
        final List<GameObject> collectionBasket = new ArrayList<GameObject>(10);
        myGridSpace.collectObjectsInTile(xGrid, yGrid, collectionBasket);
        assertEquals(shouldContain, collectionBasket.contains(testObject));
    }
}
