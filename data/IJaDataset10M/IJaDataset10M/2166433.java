package it.diamonds.engine;

import it.diamonds.engine.video.Texture;

public interface AbstractEngine {

    int getDisplayWidth();

    int getDisplayHeight();

    void shutDown();

    boolean isWindowClosed();

    void updateDisplay();

    void clearDisplay();

    void drawQuad(Point position, float width, float height, Texture texture, Rectangle textureRect);
}
