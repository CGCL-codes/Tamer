package shapes3d.animation;

import processing.core.PApplet;
import processing.core.PVector;
import shapes3d.Shape3D;

/**
 * Class to cause a shape to move from one position to another over a fixed
 * time period and after an optional delay start time.<Bbr>
 * 
 * @author Peter Lager
 *
 */
public class ShapeMover extends AbstractVectorChangeAction {

    /**
	 * Create and initialise the auto mover. <br>
	 * 
	 * @param theApp
	 * @param shape
	 * @param start the start position
	 * @param end the desired position
	 * @param duration time allowed to move the shape
	 */
    public ShapeMover(PApplet theApp, Shape3D shape, PVector start, PVector end, float duration, float delay) {
        super(theApp, shape, start, end, duration, delay);
    }

    /**
	 * Update the position once per frame.
	 */
    public void pre() {
        PVector newValue = update();
        if (newValue != null) shape.moveTo(newValue);
    }
}
