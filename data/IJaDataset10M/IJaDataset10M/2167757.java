package view.effects;

import java.awt.Graphics2D;

/**
 * ComponentEffect that moves a component from its position in the start
 * state to its position in the end state, based on linear interpolation
 * between the two points during the time of the animated transition.
 *
 * The class extends ComponentImageEffect to use a simple image copy
 * for rendering the component rather than re-rendering the actual
 * component during each frame.
 *
 * @author Chet Haase
 */
public class Move extends ComponentEffect {

    /**
     * REMIND: docs
     */
    public Move() {
    }

    /**
     * REMIND: docs
     */
    public Move(ComponentState start, ComponentState end) {
        setComponentStates(start, end);
    }

    @Override
    public void setup(Graphics2D g2d, float fraction) {
        int x, y;
        x = (int) (start.getX() + (fraction * (end.getX() - start.getX())));
        y = (int) (start.getY() + (fraction * (end.getY() - start.getY())));
        g2d.translate(x, y);
        super.setup(g2d, fraction);
    }
}
