package org.pandcorps.game.actor;

import org.pandcorps.pandam.*;
import org.pandcorps.pandam.event.StepEvent;
import org.pandcorps.pandam.event.StepListener;

public abstract class Guy2 extends Panctor implements StepListener {

    protected static final byte MODE_STILL = 0;

    protected static final byte MODE_WALK = 1;

    private static final float speed = 2;

    private static byte shadowTimer = 0;

    protected final Decoration shadow;

    protected byte mode = MODE_STILL;

    protected float dx = 0;

    protected float dy = 0;

    public static final class Guy2Type {

        private final Panmage shadowImage;

        private final int depthShadow;

        public Guy2Type(final Panmage shadowImage, final int depthShadow) {
            this.shadowImage = shadowImage;
            this.depthShadow = depthShadow;
        }
    }

    protected Guy2(final String id, final Panroom room, final Guy2Type type) {
        super(id);
        shadow = new Decoration(id + ".shadow");
        shadow.setView(type.shadowImage);
        shadow.getPosition().setZ(type.depthShadow);
        room.addActor(shadow);
        room.addActor(this);
    }

    @Override
    public void onStep(final StepEvent event) {
        final Panple pos = getPosition();
        switch(mode) {
            case MODE_WALK:
                if (dx != 0) {
                    setMirror(dx < 0);
                }
                changeView(getWalk());
                mode = MODE_STILL;
                break;
            case MODE_STILL:
                changeView(getStill());
                break;
            default:
                handleMode(mode);
                break;
        }
        final Panple min = getMin(), max = getMax();
        pos.add(dx, dy, min.getX(), min.getY(), max.getX(), max.getY());
        pos.setZ(-pos.getY());
        shadow.getPosition().set(pos.getX(), pos.getY());
        shadow.setVisible(areShadowsVisible());
        shadow.setMirror(isMirror());
        dx = 0;
        dy = 0;
    }

    protected void handleMode(final byte mode) {
    }

    protected abstract Panimation getStill();

    protected abstract Panimation getWalk();

    protected abstract Panple getMin();

    protected abstract Panple getMax();

    public final void walkDown() {
        walk(0, -speed);
    }

    public final void walkUp() {
        walk(0, speed);
    }

    public final void walkLeft() {
        walk(-speed, 0);
    }

    public final void walkRight() {
        walk(speed, 0);
    }

    private final void walk(final float dx, final float dy) {
        if (isFree()) {
            this.dx += dx;
            this.dy += dy;
            mode = MODE_WALK;
        }
    }

    protected final boolean isFree() {
        return mode == MODE_STILL || mode == MODE_WALK;
    }

    public static final void step() {
        shadowTimer++;
        if (shadowTimer > 4) {
            shadowTimer = 0;
        }
    }

    public static final boolean areShadowsVisible() {
        return shadowTimer == 0;
    }

    @Override
    protected void onDestroy() {
        shadow.destroy();
    }
}
