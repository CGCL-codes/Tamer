package src.objects;

import java.util.Vector;
import src.engine.PICAIUAnimation;

/**
 *
 * @author Owner
 */
public class PICAIUBounty extends PICAIUGameObject_S {

    protected PICAIUAnimation animation;

    private int type;

    private int value;

    public static final int MOVE = 0;

    public static final int RUN = 1;

    public static final int ATTACK = 2;

    private PICAIUGameUnit attached;

    public PICAIUBounty(PICAIUGameUnit u, int value) {
        attached = u;
        this.value = value;
        this.type = ATTACK;
        init();
    }

    public PICAIUBounty(int mx, int my, int type, int value) {
        position.setX(mx - 8);
        position.setY(my - 9);
        this.type = type;
        this.value = value;
        init();
    }

    private void init() {
        animation = new PICAIUAnimation("bounty.gif", 16, 80);
        selectionBounds.add(new PICAIURectBounds(0, 0, 15, 17));
        selectable = true;
        tMiddle = 8;
    }

    @Override
    public void update(long deltaT) {
        animation.update(deltaT);
    }

    /**
     * Returns the name of the current animation
     */
    @Override
    public String getAnimationName() {
        return animation.getAnimation();
    }

    public int getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    @Override
    public double getX() {
        if (attached == null) {
            return position.getX();
        } else {
            return attached.getX() + 8;
        }
    }

    @Override
    public double getY() {
        if (attached == null) {
            return position.getY();
        } else {
            return attached.getY() - 17;
        }
    }

    public boolean isAttached() {
        return attached != null;
    }

    public PICAIUGameUnit getAttachedUnit() {
        return attached;
    }
}
