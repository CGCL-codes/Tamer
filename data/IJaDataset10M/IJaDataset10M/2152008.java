package edu.cmu.relativelayout.matrix;

/**
 * Indicates that the constraints specified for objects that have been added to the layout could describe more than one
 * possible arrangement of the controls. This is often the result of adding "circular dependencies" to the layout, such
 * that the position of control <code>A</code> depends on the position of control <code>B</code>, and the position
 * of control <code>B</code> also depends on the position of control <code>A</code>. Typically, one of these
 * bindings can be safely removed depending on which control's position or size is actually being defined by that
 * binding.
 * 
 * @author Rachael Bennett (srbennett@gmail.com)
 */
public class AmbiguousLayoutException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AmbiguousLayoutException() {
        super();
    }

    @Override
    public String getMessage() {
        return "The constraints specified for this layout could result in multiple layouts.";
    }
}
