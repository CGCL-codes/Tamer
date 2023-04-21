package org.jfge.ext.physics;

import org.jfge.api.fighter.Fighter;
import org.jfge.api.sprite.Sprite;
import org.jfge.spi.physics.SpritePhysics;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * The Class JumpBackward.
 */
public final class JumpBackward implements SpritePhysics<Fighter> {

    /** The FLOOR. */
    private static int FLOOR = 240;

    /** The sprite. */
    private Fighter parent;

    /** The HORIZONTAL. */
    private final int HORIZONTAL;

    /** The VERTICAL. */
    private final int VERTICAL;

    private int direction;

    /**
	 * Instantiates a new jump backward.
	 *
	 * @param horizontal the horizontal
	 * @param vertical the vertical
	 */
    @Inject
    public JumpBackward(@Named("physics.fighter.jump.horizontal") int horizontal, @Named("physics.fighter.jump.vertical") int vertical) {
        this.HORIZONTAL = horizontal;
        this.VERTICAL = vertical;
    }

    @Override
    public void setParent(Fighter sprite) {
        this.parent = sprite;
    }

    @Override
    public void update() {
        if (parent == null) return;
        if (direction == Sprite.RIGHT) {
            parent.setDx(-HORIZONTAL);
        } else {
            parent.setDx(HORIZONTAL);
        }
        if (parent.getDy() > 0) {
            parent.setDy(VERTICAL);
        } else {
            parent.setDy(-VERTICAL);
        }
        if (parent.getY() - parent.getHeight() < 0) {
            parent.setDy(VERTICAL);
        }
        if (parent.getY() + parent.getDy() > FLOOR) {
            parent.setDx(0);
            parent.setDy(0);
            parent.setY(FLOOR);
            parent.nextState();
        }
    }

    @Override
    public void init() {
        if (this.parent == null) return;
        this.direction = parent.getDirection();
    }
}
