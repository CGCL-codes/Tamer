package infinitewisdom.view;

import java.awt.Graphics;
import infinitewisdom.model.GameWorld;

/**
 * Create overlays for the {@link GameWorldView} widget.
 * The <u>getGameWorldView() </u> method usually doesn't get called, it's just there to remind you
 * that you'll need a reference to GameWorldView in order to convert between screen and gameworld 
 * coordinates. You also need a reference to {@link GameWorld} too, but you can get that through
 * GameWorldView anyway.
 * @author levente
 *
 */
public interface OverlayIF {

    public GameWorldView getGameWorldView();

    public abstract void draw(Graphics g);
}
