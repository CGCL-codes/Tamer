package it.diamonds.tests.playfield;

import it.diamonds.engine.Rectangle;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.playfield.CounterBox;
import it.diamonds.tests.mocks.MockEngine;
import junit.framework.TestCase;

public class TestCounterBox extends TestCase {

    private AbstractEngine engine;

    private CounterBox counterBox;

    protected void setUp() throws Exception {
        super.setUp();
        engine = new MockEngine(800, 600);
        counterBox = CounterBox.createForPlayerOne(engine);
    }

    public void testHiddenAfterCreation() {
        assertTrue("new CounterBox must be hidden", counterBox.isHidden());
    }

    public void testCorrectTexture() {
        assertEquals("CounterBox must be created with correct texture", "gfx/layout/counter", counterBox.getTexture().getName());
    }

    public void testCorrectPosition() {
        assertEquals("CounterBox playerOne must be created with correct X position", 120, counterBox.getPosition().getX(), 0.0001f);
        assertEquals("CounterBox playerOne must be created with correct y position", 492, counterBox.getPosition().getY(), 0.0001f);
        counterBox = CounterBox.createForPlayerTwo(engine);
        assertEquals("CounterBox playerOne must be created with correct X position", 521, counterBox.getPosition().getX(), 0.0001f);
        assertEquals("CounterBox playerOne must be created with correct y position", 492, counterBox.getPosition().getY(), 0.0001f);
    }

    public void testIsDrawnWithCorrectRectangle() {
        counterBox.show();
        counterBox.draw(engine);
        assertEquals("COunterBox drawn with wrong Rectangle", new Rectangle(0, 0, 171, 58), ((MockEngine) engine).getImageRect());
    }
}
