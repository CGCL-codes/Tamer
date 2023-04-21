package zilvic.resources.sprites;

import java.awt.Graphics;
import java.util.ArrayList;
import org.duncan.Math2D.Point2D;
import zilvic.resources.ByteMagic;
import zilvic.resources.Parsable;

/**
 *
 * @author Duncan
 */
public class Sequence implements Drawable, Parsable {

    private ArrayList<Sprite> sprites = new ArrayList<Sprite>();

    public Sequence() {
    }

    public Sprite get(int index) {
        if (index < 0 || sprites.isEmpty()) return null;
        index = index % sprites.size();
        return sprites.get(index);
    }

    public Sequence add(Sprite sprite) {
        sprites.add(sprite);
        return this;
    }

    /**
     * Draw the sprite onto the graphics context provided
     *
     * @param g The graphics context on which to draw the sprite
     * @param position The location at which to draw the sprite
     * @param step The step of the animation. Can be either 0, 1, 2, or 3.
     * @param direction The direction of the sprite. THIS PARAMETER IS NOT USED
     * IN THIS CLASS.
     * @See Actor
     * @return A reference to this.
     */
    public Sequence draw(Graphics g, Point2D position, byte step, byte direction) {
        get(step).draw(g, position, step, direction);
        return this;
    }

    /**
     * The format is as follows.
     * 0:  The first sprite
     * ...
     * @return This effect in bytes.
     */
    public byte[] toBytes() {
        byte[][] bytes = new byte[sprites.size()][];
        for (int i = 0; i < sprites.size(); i++) {
            bytes[i] = sprites.get(i).getName().getBytes();
        }
        return ByteMagic.compressBytes(bytes);
    }

    public void fromBytes(byte[] bytes) {
        byte[][] u = ByteMagic.uncompressBytes(bytes);
        sprites.clear();
        for (int i = 0; i < u.length; i++) {
            sprites.add(SpriteStore.get().getSprite(ByteMagic.bytesToString(u[i])));
        }
    }
}
