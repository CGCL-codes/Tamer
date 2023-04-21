package issrg.pba;

/**
 * This class implements the Credentials: the initiator's Access Decision
 * Information (ADI) in terms of the ISO 10181-3 access control
 * (authorisation) framework.
 *
 * <p>Note that the standard does not restrict what the initiator's ADI
 * should be. We suggest it could be the initiator-bound data that specifies
 * certain access privileges, like security labels, user group inclusion
 * statements, role assignments, etc. Note that we do not limit the ADI
 * to the stated examples.
 *
 * <p>The ADI is represented as a set of values, this allows verification
 * against the policy ( via set comparison) that the initiator has not done
 * more than is allowed.
 *
 * <p>The caller should know if the object is a superset
 * of another object (ie. the object contains another credential). This is
 * sufficient for decision-making and delegation. Since the Credentials are a set,
 * the two other methods intersection and union help optimise the operations.
 *
 * @author A Otenko
 * @version 1.0
 */
public interface Credentials extends Cloneable {

    /**
	 * This method tells if the Credentials contain the given subset of
	 * Credentials.
	 *
	 * @param subSet is the set to test against this
	 *
	 * @return true, if this set contains the given subset, or is equal to
	 *    it; returns false if the subset is not wholly contained or is not equal
	 */
    public boolean contains(Credentials subSet);

    /**
	 * This method returns the intersection of this set with the given set.
	 * It is supposed the operands are replaceable: <code>this.intersection(set)</code>
	 * should be equal to <code>set.intersection(this)</code> - it may
	 * produce an internally different object, but it should behave in the same way
	 * and equals method should return true when comparing such results.
	 *
	 * @param set is the set to intersect with
	 *
	 * @return the intersection Credentials; the resulting set is contained within
	 *    both this Credentials and the given set
	 */
    public Credentials intersection(Credentials set);

    /**
	 * This method returns the union of this set with the given set.
	 * It is supposed the operands are replaceable: <code>this.union(set)</code>
	 * should be equal to <code>set.union(this)</code> - it may
	 * produce an internally different object, but it should behave in the same way
	 * and equals method should return true when comparing such results.
	 *
	 * @param set is the set to join with
	 *
	 * @return the union Credentials; the resulting set contains both this
	 *    Credentials and the given set
	 */
    public Credentials union(Credentials set);

    /**
         * This method creates a copy of the Credentials. It is essential
         * for computing intermediate results.
         *
         * @return a copy of the credentials object
         */
    public Object clone();
}
