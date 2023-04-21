package abricots.entity.drawer;

import abricots.entity.state.EntityState;
import org.newdawn.slick.Graphics;

/**
 *
 * @author charly
 */
public interface EntityDrawer {

    public void draw(EntityState state, Graphics g);
}
