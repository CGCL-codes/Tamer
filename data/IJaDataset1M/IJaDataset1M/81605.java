package test.common.model.map;

import common.model.Direction;
import common.model.map.interfaces.Tile;
import common.model.map.interfaces.TileMap;
import common.model.map.interfaces.TileMover;
import common.model.unit.Unit;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import test.TestUtil;

/**
 * Test functions in the TileMap2D class
 *
 * @author Stefan
 * @since 2.0
 */
public class TileMapTest {

    private TileMap map;

    @Before
    public void beforeEachTest() {
        map = TestUtil.getMap();
    }

    @Test
    public void loopThroughAllTiles() {
        int nTiles = 0;
        for (Tile t : map.getAllTiles()) {
            Assert.assertNotNull(t);
            Assert.assertNotNull(t.getTerrain());
            nTiles++;
        }
        int realCount = map.countTiles();
        Assert.assertEquals(realCount, nTiles);
    }

    @Test
    public void checkUnitTileLocationWithMapLocation() {
        map.getTile(0, 0).add(TestUtil.getDefaultInfantry());
        map.getTile(2, 0).add(TestUtil.getMechHpOnly(20));
        map.getTile(3, 2).add(TestUtil.getAPC(20));
        for (Tile t : map.getAllTiles()) {
            Unit u = (Unit) t.getTileMover();
            if (u != null) {
                Assert.assertEquals(u.getLocation(), t);
            }
        }
    }

    @Test
    public void isWithinMapBounds() {
        Tile leftCorner = map.getTile(0, 0);
        Tile tile = map.getTile(0, 1);
        Assert.assertFalse(map.isWithinMapBounds(map.getTile(-1, -1)));
        Assert.assertTrue(map.isWithinMapBounds(leftCorner));
        Assert.assertTrue(map.isAdjacent(leftCorner, tile));
    }

    @Test
    public void isAdjacent() {
        Tile left = map.getTile(0, 1);
        Tile up = map.getTile(1, 0);
        Tile right = map.getTile(2, 1);
        Tile down = map.getTile(1, 2);
        Tile center = map.getTile(1, 1);
        Assert.assertTrue(map.isAdjacent(left, center));
        Assert.assertTrue(map.isAdjacent(up, center));
        Assert.assertTrue(map.isAdjacent(right, center));
        Assert.assertTrue(map.isAdjacent(down, center));
        Assert.assertFalse(map.isAdjacent(down, up));
        Assert.assertFalse(map.isAdjacent(left, right));
        Assert.assertFalse(map.isAdjacent(up, left));
        Assert.assertFalse(map.isAdjacent(up, right));
        Assert.assertFalse(map.isAdjacent(up, down));
    }

    /**
   * Loop through all tiles that touch the baseTile 5,0
   * Since the tile is the top row, the Iterator should ignore the north side
   * and not return it.
   */
    @Test
    public void adjacentIteratorEdgeOfMap1() {
        Tile topEdge = map.getTile(5, 0);
        for (Tile t : map.getSurroundingTiles(topEdge, 1, 1)) {
            boolean eastTile = t.getCol() == 6 && t.getRow() == 0;
            boolean southTile = t.getCol() == 5 && t.getRow() == 1;
            boolean westTile = t.getCol() == 4 && t.getRow() == 0;
            Assert.assertNotNull(t);
            Assert.assertEquals(true, eastTile || westTile || southTile);
        }
    }

    /**
   * Loop through all tiles that touch the leftTop tile 0,0
   * Iterating around the left top should return 2 Tiles east and south.
   * Ignoring North and West
   */
    @Test
    public void adjacentIteratorEdgeOfMap2() {
        Tile leftTop = map.getTile(0, 0);
        for (Tile t : map.getSurroundingTiles(leftTop, 1, 1)) {
            boolean eastTile = t.getCol() == 1 && t.getRow() == 0;
            boolean southTile = t.getCol() == 0 && t.getRow() == 1;
            Assert.assertNotNull(t);
            Assert.assertEquals(true, eastTile || southTile);
        }
    }

    @Test
    public void getDirectionTo() {
        Direction dir;
        Tile baseTile = map.getTile(5, 5);
        Tile left = map.getTile(4, 5);
        Tile right = map.getTile(6, 5);
        Tile up = map.getTile(5, 4);
        Tile down = map.getTile(5, 6);
        dir = map.getDirectionTo(baseTile, left);
        Assert.assertEquals(dir, Direction.WEST);
        dir = map.getDirectionTo(baseTile, right);
        Assert.assertEquals(dir, Direction.EAST);
        dir = map.getDirectionTo(baseTile, up);
        Assert.assertEquals(dir, Direction.NORTH);
        dir = map.getDirectionTo(baseTile, down);
        Assert.assertEquals(dir, Direction.SOUTH);
        dir = map.getDirectionTo(baseTile, baseTile);
        Assert.assertEquals(dir, Direction.STILL);
        dir = map.getDirectionTo(left, right);
        Assert.assertEquals(dir, Direction.STILL);
    }

    @Test
    public void teleportTest() {
        Tile from = map.getTile(0, 0);
        Tile to = map.getTile(5, 0);
        TileMover mover = TestUtil.getDefaultInfantry();
        from.add(mover);
        map.teleport(from, to);
        Assert.assertNotNull(to.getTileMover());
        Assert.assertNotNull(to.getTileMover());
        Assert.assertNotNull(to.getTileMover().getLocation());
        Assert.assertEquals(to, to.getTileMover().getLocation());
    }
}
