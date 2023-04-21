package visad.data.in;

import visad.*;

/**
 * Provides support for complementary conditions for a VisAD data object.
 *
 * <P>Instances are immutable.</P>
 *
 * @author Steven R. Emmerson
 */
public class AndCondition extends Condition {

    private final Condition conditionA;

    private final Condition conditionB;

    /**
     * Constructs from two, necessary conditions for a VisAD data object.
     * VisAD data objects that satisfy both conditions will satisfy this
     * condition.
     *
     * @param conditionA	A condition for a VisAD data object.
     * @param conditionB	A condition for a VisAD data object.
     */
    protected AndCondition(Condition conditionA, Condition conditionB) {
        this.conditionA = conditionA;
        this.conditionB = conditionB;
    }

    /**
     * Returns an instance of this class.  Constructs from two, necessary
     * conditions for a VisAD data object.  VisAD data objects that satisfy both
     * conditions will satisfy this condition.
     *
     * @param conditionA	A condition for a VisAD data object.
     * @param conditionB	A condition for a VisAD data object.
     * @return			An instance of this class.
     */
    public static AndCondition andCondition(Condition conditionA, Condition conditionB) {
        return new AndCondition(conditionA, conditionB);
    }

    /**
     * Indicates if a VisAD data object satisfies this condition.
     *
     * @param data		A VisAD data object.
     * @return			<code>true</code> if and only if the VisAD data
     *				object satisfies both the conditions used
     *				during this instance's construction.
     */
    public boolean isSatisfied(DataImpl data) {
        return conditionA.isSatisfied(data) && conditionB.isSatisfied(data);
    }
}
