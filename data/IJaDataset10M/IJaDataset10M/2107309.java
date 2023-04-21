package jasel.ui.gui;

import jasel.av.JaselColor;
import jasel.engine.Renderer;
import jasel.gui.Slider;
import jasel.ui.UI;

/**
 * 
 */
public class SliderUI implements UI<Slider> {

    private static final JaselColor TROUGH_COLOR = JaselColor.Blue;

    private static final JaselColor HANDLE_COLOR = JaselColor.Green;

    private static final int HANDLE_SIZE = 12;

    public float getTroughWidth() {
        return 6;
    }

    public void draw(Slider s, long millis, Renderer r) {
        float w = s.getWidth();
        float h = s.getHeight();
        float x = s.getHitArea().getLocation().getX() - w / 2;
        float y = s.getHitArea().getLocation().getY() - h / 2;
        r.drawQuad(TROUGH_COLOR, x, y, w, h);
        float position = s.getPosition();
        float handleX, handleY;
        if (s.getOrientation() == Slider.VERTICAL) {
            handleX = x - HANDLE_SIZE / 2 + w / 2;
            handleY = y + position - HANDLE_SIZE / 2;
            r.drawQuad(HANDLE_COLOR, handleX, handleY, HANDLE_SIZE, HANDLE_SIZE);
        } else {
            handleX = x + position - HANDLE_SIZE / 2;
            handleY = y - HANDLE_SIZE / 2 + h / 2;
            r.drawQuad(HANDLE_COLOR, handleX, handleY, HANDLE_SIZE, HANDLE_SIZE);
        }
    }
}
