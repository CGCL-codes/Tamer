package xxl.core.spatial.predicates;

import xxl.core.predicates.AbstractPredicate;
import xxl.core.spatial.rectangles.Rectangle;

/**
 *	A predicate that returns true if two Rectangles overlap
 *	in a user-specified dimension.
 *
 *  @see xxl.core.spatial.rectangles.Rectangle
 *	@see xxl.core.spatial.predicates.OverlapsPredicate
 *	@see xxl.core.spatial.cursors.PlaneSweep
 *	@see xxl.core.predicates.Predicate
 *
 */
public class DimOverlapPredicate extends AbstractPredicate {

    /** Default instance of this Object.
	*/
    public static final DimOverlapPredicate DEFAULT_INSTANCE = new DimOverlapPredicate();

    /** The dimension to consider for the overlap-operation.
	*/
    protected int dim;

    /** Creates a new DimOverlapPredicate-instance.
	 *  @param dim the dimension used to check for overlap
	 */
    public DimOverlapPredicate(int dim) {
        this.dim = dim;
    }

    /** Creates a new DimOverlapPredicate-instance (sets dim to 1).
	*/
    public DimOverlapPredicate() {
        this(1);
    }

    /** Returns true if left.overlaps(right) holds.
	 * 
	 * @param left first rectangle
	 * @param right second rectangle
	 * @return returns true if left.overlaps(right) holds
	 * 
	*/
    public boolean invoke(Object left, Object right) {
        return ((Rectangle) left).overlaps((Rectangle) right, dim);
    }
}
