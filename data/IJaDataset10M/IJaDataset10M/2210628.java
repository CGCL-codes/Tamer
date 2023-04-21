package skycastle.minigames.boardgame;

import java.awt.Graphics2D;

/**
 * Some marking or decoration located at some point on the board.
 *
 * @author Hans H�ggstr�m
 */
public interface BoardMarking {

    float getX();

    float getY();

    float getAngle_degrees();

    float getScale();

    void render(Graphics2D graphics2D);
}
