package holdtheline.core;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;

/**
 *
 * @author avincon
 */
public interface GameObjectInterface {

    public void setupScreen(GraphicsConfiguration gc, int w, int h);

    public void updateGameObject(Controls controls, long elapsed_ms, long delay_ms) throws InterruptedException;

    public void compileGameObject();

    public void renderGameObject(Graphics2D g2d);
}
