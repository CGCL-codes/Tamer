package il.co.gadiworks.games.framework;

import il.co.gadiworks.games.framework.math.Rectangle;
import il.co.gadiworks.games.framework.math.Vector2;

public class GameObject {

    public final Vector2 POSITION;

    public final Rectangle BOUNDS;

    public GameObject(float x, float y, float width, float height) {
        this.POSITION = new Vector2(x, y);
        this.BOUNDS = new Rectangle(x - width / 2, y - height / 2, width, height);
    }
}
