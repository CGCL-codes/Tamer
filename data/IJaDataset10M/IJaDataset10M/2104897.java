package tiles;

import java.awt.Image;
import interfaces.Collidable;
import core.Sprite;

/**
 * Ein Leeres Tile
 * 
 * @author Marco Strobel
 */
public class Empty extends Sprite {

    /**
	 * Konstruktor für das Tile
	 * 
	 * @param width Die Breite des Tiles
	 * @param height Die Höhe des Tiles
	 * @param image Das Bild des tiles
	 */
    public Empty(int width, int height, Image image) {
        super(image, width, height, false);
    }

    public void handleCollisionWith(Collidable other) {
        other.handleCollisionWith(this);
    }
}
