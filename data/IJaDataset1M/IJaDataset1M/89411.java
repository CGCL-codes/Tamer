package gnu.lists;

/** A predicate that (only) matches only "nodes" in the XML sense.
 * Only matches nodes in the XML "info-set".  Specifically, given
 * a sequence of primtive values only matches the first as a "text" node.
 */
public interface NodePredicate extends ItemPredicate {
}
