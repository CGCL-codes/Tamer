package netkit.graph;

/** This class keeps track of valid tokens for a CATEGORICAL attribute
 * type.  This container is mutable in the sense that you can add more
 * tokens at any time.  However existing tokens cannot be changed or
 * removed.
 * @see AttributeExpandableCategorical
 *
 * @author Kaveh R. Ghazi
 */
public class ExpandableTokenSet extends TokenSet {

    public final void add(String token) {
        super.add(token);
    }
}
