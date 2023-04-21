package org.openscience.cdk.renderer.elements.path;

/**
 * A path element.
 * 
 * @author Arvid
 * @cdk.module renderbasic
 * @cdk.githash
 */
public abstract class PathElement {

    /** the type of the path element. */
    public final Type type;

    /**
     * Create a path element.
     * 
     * @param type {@link Type} of this path element
     */
    public PathElement(Type type) {
        this.type = type;
    }

    /**
     * Get the type of the path element.
     * 
     * @return the type of the path element
     */
    public Type type() {
        return type;
    }

    /**
     * Get the points in the path.
     * 
     * @return a list of points
     */
    public abstract float[] points();
}
