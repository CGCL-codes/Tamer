package it.diamonds.tests.droppable;

import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.RUBY;
import it.diamonds.tests.grid.AbstractGridTestCase;

public class TestStoneCrush extends AbstractGridTestCase {

    public void testStoneIsCrushing() {
        insertAndUpdate(createStone(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        grid.updateCrushes();
        assertEquals(0, grid.getNumberOfDroppables());
    }

    public void testStoneIsCrushingWithDifferentType() {
        insertAndUpdate(createStone(RUBY), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        grid.updateCrushes();
        assertEquals(0, grid.getNumberOfDroppables());
    }

    public void testNotAdjacentStoneIsNotCrushing() {
        insertAndUpdate(createStone(RUBY), 13, 2);
        insertAndUpdate(createStone(RUBY), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        grid.updateCrushes();
        assertEquals(1, grid.getNumberOfDroppables());
    }

    public void testStonesCrushingOnce() {
        insertAndUpdate(createStone(RUBY), 13, 7);
        insertAndUpdate(createStone(RUBY), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        grid.updateCrushes();
        grid.updateCrushes();
        assertEquals(1, grid.getNumberOfDroppables());
    }

    public void testStoneIsNotCrushingOnChest() {
        insertAndUpdate(createStone(RUBY), 13, 7);
        insertAndUpdate(createChest(RUBY), 13, 6);
        grid.updateCrushes();
        assertEquals(2, grid.getNumberOfDroppables());
    }

    public void testTwoStonesAreCrushing() {
        insertAndUpdate(createStone(RUBY), 13, 5);
        insertAndUpdate(createStone(RUBY), 13, 6);
        insertAndUpdate(createGem(DIAMOND), 12, 5);
        insertAndUpdate(createChest(DIAMOND), 12, 6);
        grid.updateCrushes();
        assertEquals(0, grid.getNumberOfDroppables());
    }
}
