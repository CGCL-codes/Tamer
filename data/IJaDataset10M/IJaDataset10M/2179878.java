package java.util.regex;

import java.util.ArrayList;

/**
 * Negative look behind node.
 * 
 * @author Nikolay A. Kuznetsov
 */
class NegativeLookBehind extends AtomicJointSet {

    public NegativeLookBehind(ArrayList children, FSet fSet) {
        super(children, fSet);
    }

    /**
     * Returns stringIndex+shift, the next position to match
     */
    public int matches(int stringIndex, CharSequence testString, MatchResultImpl matchResult) {
        int size = children.size();
        int shift;
        matchResult.setConsumed(groupIndex, stringIndex);
        for (int i = 0; i < size; i++) {
            AbstractSet e = (AbstractSet) children.get(i);
            shift = e.findBack(0, stringIndex, testString, matchResult);
            if (shift >= 0) {
                return -1;
            }
        }
        return next.matches(stringIndex, testString, matchResult);
    }

    public boolean hasConsumed(MatchResultImpl matchResult) {
        return false;
    }

    protected String getName() {
        return "NegBehindJointSet";
    }
}
