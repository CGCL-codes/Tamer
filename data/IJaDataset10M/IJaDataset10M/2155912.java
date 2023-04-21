package it.diamonds.tests.gems;

import static it.diamonds.gems.DroppableColor.*;
import it.diamonds.engine.mocks.MockEngine;
import it.diamonds.gems.BigGem;
import it.diamonds.gems.BigGemTileType;
import it.diamonds.gems.Droppable;
import it.diamonds.grid.Grid;
import it.diamonds.tests.GridTestCase;

public class TestBigGemDrawing extends GridTestCase {

    private MockEngine engine;

    public void setUp() throws Exception {
        super.setUp();
        engine = MockEngine.createForTesting(800, 600);
        grid = Grid.createForTesting();
    }

    public void testBottomLeftTileDraw() {
        BigGem bigGem = new BigGem(0, 0, DIAMOND);
        bigGem.draw(engine, BigGemTileType.BOTTOM_LEFT_CORNER);
        assertEquals("drawn in the wrong position", 0, engine.getTextureRect().left());
        assertEquals("drawn in the wrong position", Droppable.DROPPABLE_SIZE * 2, engine.getTextureRect().top());
    }

    public void testBottomTileDraw() {
        BigGem bigGem = new BigGem(0, 0, DIAMOND);
        bigGem.draw(engine, BigGemTileType.BOTTOM_EDGE);
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE, engine.getTextureRect().left());
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE * 2, engine.getTextureRect().top());
    }

    public void testBottomRightTileDraw() {
        BigGem bigGem = new BigGem(0, 0, DIAMOND);
        bigGem.draw(engine, BigGemTileType.BOTTOM_RIGHT_CORNER);
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE * 2, engine.getTextureRect().left());
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE * 2, engine.getTextureRect().top());
    }

    public void testLeftTileDraw() {
        BigGem bigGem = new BigGem(0, 0, DIAMOND);
        bigGem.draw(engine, BigGemTileType.LEFT_EDGE);
        assertEquals("bad texture drawn", 0, engine.getTextureRect().left());
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE, engine.getTextureRect().top());
    }

    public void testInnerTileDraw() {
        BigGem bigGem = new BigGem(0, 0, DIAMOND);
        bigGem.draw(engine, BigGemTileType.INNER);
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE, engine.getTextureRect().left());
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE, engine.getTextureRect().top());
    }

    public void testRightTileDraw() {
        BigGem bigGem = new BigGem(0, 0, DIAMOND);
        bigGem.draw(engine, BigGemTileType.RIGHT_EDGE);
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE * 2, engine.getTextureRect().left());
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE, engine.getTextureRect().top());
    }

    public void testTopLeftTileDraw() {
        BigGem bigGem = new BigGem(0, 0, DIAMOND);
        bigGem.draw(engine, BigGemTileType.TOP_LEFT_CORNER);
        assertEquals("bad texture drawn", 0, engine.getTextureRect().left());
        assertEquals("bad texture drawn", 0, engine.getTextureRect().top());
    }

    public void testTopTileDraw() {
        BigGem bigGem = new BigGem(0, 0, DIAMOND);
        bigGem.draw(engine, BigGemTileType.TOP_EDGE);
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE, engine.getTextureRect().left());
        assertEquals("bad texture drawn", 0, engine.getTextureRect().top());
    }

    public void testTopRightTileDraw() {
        BigGem bigGem = new BigGem(0, 0, DIAMOND);
        bigGem.draw(engine, BigGemTileType.TOP_RIGHT_CORNER);
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE * 2, engine.getTextureRect().left());
        assertEquals("bad texture drawn", 0, engine.getTextureRect().top());
    }

    public void testGridDrawsBigGem() {
        insertAndUpdate(createGem(EMERALD), 13, 1);
        insertAndUpdate(createGem(EMERALD), 13, 2);
        insertAndUpdate(createGem(EMERALD), 13, 3);
        insertAndUpdate(createGem(EMERALD), 12, 1);
        insertAndUpdate(createGem(EMERALD), 12, 2);
        insertAndUpdate(createGem(EMERALD), 12, 3);
        insertAndUpdate(createGem(EMERALD), 11, 1);
        insertAndUpdate(createGem(EMERALD), 11, 2);
        insertAndUpdate(createGem(EMERALD), 11, 3);
        grid.updateBigGems();
        grid.draw(engine);
        assertEquals("GemGroup wasn't drawn correctly", 10, engine.getNumberOfQuadsDrawn());
        assertEquals("GemGroup must be drawn with the correct Texture", grid.getBigGemAt(13, 1).getTexture().getName(), engine.getTexture().getName());
    }

    public void testBigGemDrawnInCorrectPosition() {
        insertAndUpdate(createGem(EMERALD), 13, 5);
        insertAndUpdate(createGem(EMERALD), 13, 6);
        insertAndUpdate(createGem(EMERALD), 12, 5);
        insertAndUpdate(createGem(EMERALD), 12, 6);
        grid.updateBigGems();
        grid.draw(engine);
        Droppable topRight = grid.getGemAt(12, 6);
        assertEquals("GemGroup wasn't drawn in correct position (X)", topRight.getSprite().getX(), engine.getQuadPosition().getX());
        assertEquals("GemGroup wasn't drawn in correct position (Y)", topRight.getSprite().getY(), engine.getQuadPosition().getY());
    }

    public void testBigGemDrawsCorrectTile() {
        insertAndUpdate(createGem(EMERALD), 13, 5);
        insertAndUpdate(createGem(EMERALD), 13, 6);
        insertAndUpdate(createGem(EMERALD), 12, 5);
        insertAndUpdate(createGem(EMERALD), 12, 6);
        grid.updateBigGems();
        grid.draw(engine);
        assertEquals("bad texture drawn", Droppable.DROPPABLE_SIZE * 2, engine.getTextureRect().left());
        assertEquals("bad texture drawn", 0, engine.getTextureRect().top());
    }

    public void testNonDroppedGems() {
        insertAndUpdate(createGem(EMERALD), 13, 4);
        insertAndUpdate(createGem(EMERALD), 13, 5);
        insertAndUpdate(createGem(EMERALD), 12, 4);
        insertAndUpdate(createGem(EMERALD), 12, 5);
        insertAndUpdate(createGem(EMERALD), 13, 6);
        insertAndUpdate(createGem(EMERALD), 11, 6);
        grid.updateBigGems();
        assertFalse(grid.isCellInABigGem(12, 6));
    }
}
