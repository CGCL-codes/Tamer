package nr.co.mhgames.polyanim.interpolation;

/**
 * Power curve interpolator, raises the interpolated value to a defined power.
 * Use powers < 1.0f to have a curve that starts quick and slows down. Use
 * powers > 1.0f to have a curve that starts slow and speeds up.
 * <p>
 * When B is some value, and A a value between [0, B]: <b>Result = (A / B) ^
 * POWER</b>
 * 
 * @author Mika Halttunen
 */
public final class PowerCurveInterpolator implements Interpolator {

    /** Power value, default is 1.0f -> linear interpolation */
    private float power;

    /**
	 * Constructor.
	 */
    public PowerCurveInterpolator() {
        power = 1.0f;
    }

    /**
	 * Constructor.
	 * 
	 * @param power
	 *            Power value to raise to
	 */
    public PowerCurveInterpolator(float power) {
        this.power = power;
    }

    /**
	 * Set the power value to raise to.
	 * 
	 * @param power
	 *            Power value
	 */
    public final void setPower(float power) {
        this.power = power;
    }

    /**
	 * Return the power value to raise to.
	 * 
	 * @return Power value
	 */
    public final float getPower() {
        return power;
    }

    /**
	 * Interpolates linearly between the values and raises the result to the
	 * specified power: <code>result = (pos / maxPos) ^ power</code>.
	 * 
	 * @see {@link Interpolator#interpolate(float, float)}
	 */
    public final float interpolate(float pos, float maxPos) {
        return (float) Math.pow(pos / maxPos, power);
    }

    public final void parseFrom(String params) {
        power = 1.0f;
        try {
            power = Float.parseFloat(params);
        } catch (NumberFormatException e) {
            power = 1.0f;
        }
    }
}
