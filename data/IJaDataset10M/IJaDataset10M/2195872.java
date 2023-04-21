package pcgen.base.term;

public interface TermEvaluatorBuilder {

    String getTermConstructorPattern();

    String[] getTermConstructorKeys();

    boolean isEntireTerm();

    /**
	 * @param expressionString the term being processed
	 * @param src the source (class, race, etc. ) of the formula that this term is from
	 * @param matchedSection The portion at the start of expressionString that matched the term's pattern
	 * @return a term evaluator
	 * @throws TermEvaulatorException If the term does not parse properly, this error is thrown
	 */
    TermEvaluator getTermEvaluator(String expressionString, String src, String matchedSection) throws TermEvaulatorException;
}
