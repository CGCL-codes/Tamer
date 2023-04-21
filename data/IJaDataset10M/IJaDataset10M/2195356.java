package java.util.regex;

/**
 * Composite (i.e. {n,m}) quantifier node for groups ("(X){n,m}")
 * 
 * @author Nikolay A. Kuznetsov
 */
class CompositeGroupQuantifierSet extends GroupQuantifierSet {

    protected Quantifier quantifier = null;

    int setCounter;

    /**
     * Constructs CompositeGroupQuantifierSet
     * @param quant    - given composite quantifier
     * @param innerSet - given group
     * @param next     - next set after the quantifier
     */
    public CompositeGroupQuantifierSet(Quantifier quant, AbstractSet innerSet, AbstractSet next, int type, int setCounter) {
        super(innerSet, next, type);
        this.quantifier = quant;
        this.setCounter = setCounter;
    }

    public int matches(int stringIndex, CharSequence testString, MatchResultImpl matchResult) {
        int enterCounter = matchResult.getEnterCounter(setCounter);
        if (!innerSet.hasConsumed(matchResult)) return next.matches(stringIndex, testString, matchResult);
        if (enterCounter >= quantifier.max()) {
            return next.matches(stringIndex, testString, matchResult);
        }
        matchResult.setEnterCounter(setCounter, ++enterCounter);
        int nextIndex = innerSet.matches(stringIndex, testString, matchResult);
        if (nextIndex < 0) {
            matchResult.setEnterCounter(setCounter, --enterCounter);
            if (enterCounter >= quantifier.min()) {
                return next.matches(stringIndex, testString, matchResult);
            } else {
                matchResult.setEnterCounter(setCounter, 0);
                return -1;
            }
        } else {
            matchResult.setEnterCounter(setCounter, 0);
            return nextIndex;
        }
    }

    public void reset() {
        quantifier.resetCounter();
    }

    protected String getName() {
        return quantifier.toString();
    }

    void setQuantifier(Quantifier quant) {
        this.quantifier = quant;
    }
}
