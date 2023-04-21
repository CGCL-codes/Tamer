package it.diamonds.tests;

import it.diamonds.Background;
import it.diamonds.engine.mocks.MockEngine;
import junit.framework.TestCase;

public class TestBackground extends TestCase {

    private Background background;

    public void setUp() {
        background = new Background("back000", ".jpg");
    }

    public void testBackgroundPosition() {
        assertEquals("X origin must be 0", 0F, background.getX());
        assertEquals("Y origin must be 0", 0F, background.getY());
    }

    public void testBackgroundWidthAndHeight() {
        assertEquals("Width must be equal to the width of the window", 1024, background.getWidth());
        assertEquals("Height must be equal to the height of the window", 1024, background.getHeight());
    }

    public void testBackgroundSpriteNotNull() {
        assertNotNull(background.getSprite());
    }

    public void testDraw() {
        MockEngine engine = new MockEngine();
        background.draw(engine);
        assertEquals(1, engine.getNumberOfQuadsDrawn());
    }
}
