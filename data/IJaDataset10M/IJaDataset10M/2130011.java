package net.sf.opengroove.client.ui;

import java.awt.Point;

/**
 * This class contains utilities for doing general animations, as well as some
 * methods for preforming animations on swing components.<br/><br/>
 * 
 * As of 2008.09.10, this class is no longer supported. It, or portions of it,
 * will be moved into the net.sf.opengroove.client.ui.transitions package, which
 * is replacing it.
 * 
 * @author Alexander Boyd
 * 
 */
public class Animations {

    /**
     * A set of functions for generating scale positions based on the current
     * step. When an animation method is called, the getValue() method on one of
     * these is called repeatedly with incrementing values of the step passed
     * in. The value returned is then multiplied into the animation to create
     * the smooth effect.
     * 
     * @author Alexander Boyd
     * 
     */
    public static enum ScaleFunction {

        /**
         * A sine function. Returns the sine of the argument, translating the
         * possible values of the argument into the range -90 to 90.
         */
        SINE {

            public double getValue(double step) {
                return (Math.sin(Math.toRadians((step * 180) - 90)) + 1.0) / 2.0;
            }
        }
        , /**
         * A double-sine function. Returns SINE.getValue(SINE.getValue(step)).
         */
        DOUBLE_SINE {

            public double getValue(double step) {
                return SINE.getValue(SINE.getValue(step));
            }
        }
        , /**
         * A linear function. It just returns <code>step</code>.
         */
        LINEAR {

            public double getValue(double step) {
                return step;
            }
        }
        ;

        /**
         * Transforms an input value between 0 and 1 inclusive into a
         * corresponding scale value, also between 0 and 1 inclusive.
         * 
         * @param step
         * @return
         */
        public abstract double getValue(double step);
    }

    /**
     * Generates a set of points that move the object at <code>start</code> to
     * <code>end</code> smoothly, in the number of steps specified.
     * 
     * @param start
     *            The start point
     * @param end
     *            The end point
     * @param steps
     *            The number of steps to generate, which must be at least 2
     * @return a set of points that can be followed to animate the transfer of
     *         the object at the start point to the end point. This will have a
     *         length equal to <code>steps</code>.
     */
    public static Point[] generateLineAnimation(Point start, Point end, int steps) {
        ScaleFunction sf = ScaleFunction.SINE;
        System.out.println("start:" + start + ",end:" + end + ",steps:" + steps);
        if (steps < 2) throw new IllegalArgumentException("steps must be at least 2, but it was " + steps);
        Point[] result = new Point[steps];
        double difX = end.x - start.x;
        double difY = end.y - start.y;
        double inc = 1.0 / ((steps - 1.0) * 1.0);
        System.out.println("difx:" + difX + ",dify:" + difY + ",inc:" + inc);
        for (int i = 0; i < steps; i++) {
            double pos = sf.getValue(i * inc);
            System.out.println("i:" + i + ",inc:" + inc + ",i*inc:" + (i * inc) + ",pos:" + pos);
            result[i] = new Point((int) (start.x + (difX * pos)), (int) (start.y + (difY * pos)));
        }
        return result;
    }
}
