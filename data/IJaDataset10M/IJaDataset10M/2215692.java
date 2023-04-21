package xxl.core.spatial.geometries.predicates;

import xxl.core.predicates.AbstractPredicate;
import xxl.core.spatial.geometries.Geometry2D;

/**
 *	A predicate that returns true if geometry0 equals geometry1 spatially.
 */
public class Equals extends AbstractPredicate<Geometry2D> {

    /** Default instance of this Object.
	 */
    public static Equals DEFAULT_INSTANCE = new Equals();

    /** Creates a new Crosses-instance.
	 */
    public Equals() {
        super();
    }

    /** Returns true if geometry0 equals geometry1 spatially.
	 *
	 * @param geometry0 first geometry
	 * @param geometry1 second geometry
	 * @return returns true if <tt>geometry0</tt> equals object <tt>geometry1</tt>
	 * 
	 */
    public boolean invoke(Geometry2D geometry0, Geometry2D geometry1) {
        return geometry0.equals(geometry1);
    }
}
