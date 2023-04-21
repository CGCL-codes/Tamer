package raja;

/**
 * The <code>Ray</code> class defines a high precision ray in the 3
 * dimensional space.  A ray is basically a semiline, given by its origin,
 * a <code>Point3D</code>, and its direction, a <code>Vector3D</code>.
 *
 * @see Point3D
 * @see Vector3D
 *
 * @author Emmanuel Fleury
 * @author Gr�goire Sutre
 */
public class Ray {

    /**
     * The <i>origin</i> of the ray.
     * @serial
     */
    public Point3D origin;

    /**
     * The <i>direction</i> of the ray.  This <code>Vector3D</code> is normed
     * upon construction, and it <i>should</i> stay normed.
     * @serial
     */
    public Vector3D direction;

    /**
     * Creates a <code>Ray</code> object initialized with the
     * specified origin and direction.
     * 
     * @param origin    the origin of the newly constructed
     *                  <code>Ray</code>.
     * @param direction the direction of the newly constructed
     *                  <code>Ray</code>.
     */
    public Ray(Point3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = Vector3D.normalization(direction);
    }

    /**
     * Creates a <code>Ray</code> object initialized with the
     * specified origin and whose direction connects the origin with the
     * specified destination.
     * 
     * @param origin      the origin of the newly constructed
     *                    <code>Ray</code>.
     * @param destination the destination that the direction of the newly
     *                    constructed <code>Ray</code> connects the origin
     *                    with.
     */
    public Ray(Point3D origin, Point3D destination) {
        this(origin, new Vector3D(origin, destination));
    }
}
