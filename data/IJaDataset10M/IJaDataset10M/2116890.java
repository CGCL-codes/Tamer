package com.googlecode.gchartjava;

/**
 * Class contains common code for LinearFills.
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
abstract class AbstractLinearFill implements Fill {

    /** The linear fill angle.**/
    private final int angle;

    /**
     * AbstractLinearFill constructor.
     *
     * @param angle
     *            The fill angle.
     */
    AbstractLinearFill(final int angle) {
        super();
        this.angle = angle;
    }

    /**
     * Get the angle.
     *
     * @return Get the angle integer.
     */
    final int getAngle() {
        return angle;
    }
}
