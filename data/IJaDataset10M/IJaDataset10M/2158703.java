package it.diamonds.tests.playfield;

import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableType;
import it.diamonds.droppable.GemQueue;
import it.diamonds.engine.Point;
import it.diamonds.playfield.NextGemsPanel;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockEngine;
import it.diamonds.tests.mocks.MockRandomGenerator;
import java.io.IOException;

public class TestNextGemsPanel extends AbstractGridTestCase {

    private GemQueue gemQueue;

    private NextGemsPanel indicator;

    public void setUp() throws IOException {
        super.setUp();
        int[] randomSequence = { 20, 60 };
        gemQueue = GemQueue.createForTesting(environment, new MockRandomGenerator(randomSequence));
        gemQueue.fillQueueRandomly();
        indicator = new NextGemsPanel(gemQueue, new Point(292, 32));
    }

    public void testDraw() {
        indicator.draw(environment.getEngine());
        assertEquals(2, ((MockEngine) environment.getEngine()).getNumberOfQuadsDrawn());
    }

    public void testDrawTwoGemPositionAndTexture() {
        Droppable firstGem = gemQueue.getGemAt(0);
        Droppable secondGem = gemQueue.getGemAt(1);
        DroppableType firstGemType = firstGem.getGridObject().getType();
        DroppableType secondGemType = secondGem.getGridObject().getType();
        DroppableColor firstGemColor = firstGem.getGridObject().getColor();
        DroppableColor secondGemColor = secondGem.getGridObject().getColor();
        indicator.draw(environment.getEngine());
        assertEquals("gfx/droppables/" + firstGemType.getName() + "/" + firstGemColor.getName(), ((MockEngine) environment.getEngine()).getImage(0).getName());
        assertEquals("gfx/droppables/" + secondGemType.getName() + "/" + secondGemColor.getName(), ((MockEngine) environment.getEngine()).getImage(1).getName());
        assertEquals(292f, ((MockEngine) environment.getEngine()).getQuadPosition(1).getX());
        assertEquals(32f, ((MockEngine) environment.getEngine()).getQuadPosition(1).getY());
        assertEquals(292f, ((MockEngine) environment.getEngine()).getQuadPosition(0).getX());
        assertEquals(64f, ((MockEngine) environment.getEngine()).getQuadPosition(0).getY());
    }
}
