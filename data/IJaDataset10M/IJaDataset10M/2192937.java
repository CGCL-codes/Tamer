package prajna.data;

/**
 * <P>
 * This interface provides a common set of utilities for items which can be
 * animated. While it is intended to be used for renderers, it can be used for
 * other purposes as well.
 * </p>
 * <P>
 * This class is still in development and experimentation. Methods may change
 * or be added.
 * </p>
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public interface Animatable {

    /**
     * Enumeration of values representing possible animation directives
     */
    public enum Animation {

        NONE, MOVE, ROTATE_RIGHT, ROTATE_LEFT, MOVE_ROTATE_RIGHT, MOVE_ROTATE_LEFT, SCALE, TRANSFORM
    }

    /**
     * returns the animation type flag
     * 
     * @return the flag indicating the type of transformation
     */
    public Animation getAnimationFlag();

    /**
     * returns the fractional step along the animation
     * 
     * @return the fractional step along the animation
     */
    public double getAnimationStep();

    /**
     * Sets a flag indicating the type of transformation.
     * 
     * @param flag indicating the type of transformation
     */
    public void setAnimationFlag(Animation flag);

    /**
     * Sets the fractional step along the animation. if <tt>frac</tt>=0.0,
     * then the animation is at the start position; a value of 1.0 is at the
     * end position.
     * 
     * @param frac the fractional step along the animation
     */
    public void setAnimationStep(double frac);
}
