package sabokan.game.entities.boxes.impl;

import java.awt.Image;
import sabokan.game.Constants;
import sabokan.game.entities.boxes.Box;

/**
 *
 * @author anaka
 */
public class RockBox extends Box {

    private static final Image texture = Constants.getResourceAsImage("Rock.png");

    public RockBox(int x, int y) {
        super(x, y);
    }

    @Override
    public Image getTexture() {
        return texture;
    }
}
