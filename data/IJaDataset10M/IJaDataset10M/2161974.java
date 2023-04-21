package org.socialworld.attributes;

import org.socialworld.calculation.Vector;

/**
 * The class implements a vector's
 *         interpretation as a direction. It is needed for moves of simulation
 *         objects or calculation what a simulation object can see. Furthermore
 *         every event has a range of directions that specifies what objects are
 *         affected by the event.
 * @author Mathias Sikos (tyloesand) 
 */
public class Direction extends Vector {

    public Direction() {
    }

    public Direction(double x, double y, double z) {
        super(x, y, z);
    }

    /**
	 * The method calculates the tangent of the angle between two vectors. The
	 * formula is: tan = (|a| - |b|) / ( |a - b| )
	 * 
	 * @param direction
	 * @return the tangent
	 */
    public double getAngleTangent(Direction b) {
        Direction a = this;
        double divident = a.getLength() - b.getLength();
        double divisor = a.getDifference(b).getLength();
        if (divisor == 0) return Double.NaN;
        return divident / divisor;
    }
}
