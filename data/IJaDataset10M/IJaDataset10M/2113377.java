package org.makagiga.commons.transition;

import javax.swing.JComponent;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Sine;
import org.makagiga.commons.TK;
import org.makagiga.commons.WTFError;

/**
 * @since 3.8.6
 */
public class MoveTransition extends Transition {

    public enum Direction {

        LEFT, RIGHT, UP, DOWN, AUTO_VERTICAL, AUTO_HORIZONTAL
    }

    private final Direction direction;

    public MoveTransition(final Direction direction) {
        super(500);
        this.direction = TK.checkNull(direction);
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setup(final JComponent c, final Timeline timeline, final ImageView from, final ImageView to) {
        timeline.setEase(new Sine());
        int w = c.getWidth();
        int h = c.getHeight();
        from.setX(0);
        from.setY(0);
        Direction useDirection;
        switch(getDirection()) {
            case AUTO_HORIZONTAL:
                useDirection = isNext(c, from, to) ? MoveTransition.Direction.RIGHT : MoveTransition.Direction.LEFT;
                break;
            case AUTO_VERTICAL:
                useDirection = isNext(c, from, to) ? MoveTransition.Direction.UP : MoveTransition.Direction.DOWN;
                break;
            default:
                useDirection = direction;
        }
        switch(useDirection) {
            case LEFT:
                to.setX(w);
                timeline.addPropertyToInterpolate(Timeline.<Integer>property("x").on(from).from(0).to(-w));
                timeline.addPropertyToInterpolate(Timeline.<Integer>property("x").on(to).from(to.getX()).to(0));
                break;
            case RIGHT:
                to.setX(-w);
                timeline.addPropertyToInterpolate(Timeline.<Integer>property("x").on(from).from(0).to(w));
                timeline.addPropertyToInterpolate(Timeline.<Integer>property("x").on(to).from(to.getX()).to(0));
                break;
            case UP:
                to.setY(h);
                timeline.addPropertyToInterpolate(Timeline.<Integer>property("y").on(from).from(0).to(-h));
                timeline.addPropertyToInterpolate(Timeline.<Integer>property("y").on(to).from(to.getY()).to(0));
                break;
            case DOWN:
                to.setY(-h);
                timeline.addPropertyToInterpolate(Timeline.<Integer>property("y").on(from).from(0).to(h));
                timeline.addPropertyToInterpolate(Timeline.<Integer>property("y").on(to).from(to.getY()).to(0));
                break;
            default:
                throw new WTFError(direction);
        }
    }

    private boolean isNext(final JComponent c, final ImageView from, final ImageView to) {
        JComponent oldVisible = from.getComponent();
        JComponent newVisible = to.getComponent();
        int newVisibleIndex = -1;
        int oldVisibleIndex = -1;
        for (int i = 0; i < c.getComponentCount(); i++) {
            if ((oldVisibleIndex == -1) && (c.getComponent(i) == oldVisible)) oldVisibleIndex = i;
            if ((newVisibleIndex == -1) && (c.getComponent(i) == newVisible)) newVisibleIndex = i;
        }
        return newVisibleIndex > oldVisibleIndex;
    }
}
