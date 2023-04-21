package visad.data.in;

import visad.*;

/**
 * Provides support for applying arbitrary conditions to VisAD data objects.
 * This class supports data filters like {@link Selector}.
 *
 * <P>Instances are immutable.</P>
 *
 * @author Steven R. Emmerson
 */
public abstract class Condition {

    /**
     * The trivial condition.  The {@link #isSatisfied} method of this condition
     * always returns <code>true</code>.
     */
    public static Condition TRIVIAL_CONDITION = new Condition() {

        public boolean isSatisfied(DataImpl data) {
            return true;
        }
    };

    /**
     * Indicates if a VisAD data object satisfies this condition.
     *
     * @param data		A VisAD data object.
     * @return			<code>true</code> if and only if the VisAD data
     *				object satisfies this instance's condition.
     */
    public abstract boolean isSatisfied(DataImpl data);
}
