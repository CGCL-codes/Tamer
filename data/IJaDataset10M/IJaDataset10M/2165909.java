package tiles;

import interfaces.Collidable;
import core.Sprite;

public class WallRightBelow extends Sprite {

    static String imageSource = "/images/tiles/wallrightbelow.png";

    /**
	 * @param width
	 * @param height
	 */
    public WallRightBelow(int width, int height) {
        super(imageSource, width, height, true);
    }

    public void handleCollisionWith(Collidable other) {
        other.handleCollisionWith(this);
    }
}
