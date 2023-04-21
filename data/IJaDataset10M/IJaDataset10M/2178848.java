package nr.co.mhgames.polyanim.renderer.modifiers;

/**
 * Flip modifier allows flipping the animation vertically, horizontally or both
 * ways at the same time.
 * 
 * @author Mika Halttunen
 */
public class FlipModifier implements RenderModifier {

    /** Flip directions */
    public enum FlipDirection {

        VERTICAL, HORIZONTAL, BOTH
    }

    /** Direction of the flip */
    public FlipDirection direction;

    /** Constructor */
    public FlipModifier(FlipDirection direction) {
        this.direction = direction;
    }
}
