package org.chaoticengine.cgll.entity.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Matt v.d. Westhuizen
 */
public interface IRenderComponent extends IComponent {

    public void render(GameContainer gc, StateBasedGame sb, Graphics gr);
}
