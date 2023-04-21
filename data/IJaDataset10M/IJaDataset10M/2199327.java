package xxl.core.spatial.predicates;

import xxl.core.indexStructures.Descriptor;
import xxl.core.predicates.AbstractPredicate;

/**
 *	A predicate that returns true if object0 contains object1.
 *
 *  @see xxl.core.indexStructures.Descriptor
 *  @see xxl.core.predicates.Predicate
 *
 */
public class ContainsPredicate extends AbstractPredicate<Descriptor> {

    /** Returns true if object1 is contained in object0.
	 *
	 * The method is implemented as follows:
	 * <pre><code>
	 *
	 *   public boolean invoke(Descriptor desc1, Descriptor desc2){
	 *	   return desc1.contains(desc2);
	 *   }	 
	 * </pre></code>
	 * 
	 * @param desc1 first object
	 * @param desc2 second object
	 * @return returns true if object1 is contained in object0 
	 */
    @Override
    public boolean invoke(Descriptor desc1, Descriptor desc2) {
        return desc1.contains(desc2);
    }
}
