package java.util.regex;

/**
 * Possessive quantifier set over groups.
 * 
 * @author Nikolay A. Kuznetsov
 */
class PossessiveGroupQuantifierSet extends GroupQuantifierSet {

    public PossessiveGroupQuantifierSet(AbstractSet innerSet, AbstractSet next, int type) {
        super(innerSet, next, type);
        innerSet.setNext(FSet.posFSet);
    }

    public int matches(int stringIndex, CharSequence testString, MatchResultImpl matchResult) {
        int nextIndex;
        while ((nextIndex = innerSet.matches(stringIndex, testString, matchResult)) > 0) {
            stringIndex = nextIndex;
        }
        return next.matches(stringIndex, testString, matchResult);
    }
}
