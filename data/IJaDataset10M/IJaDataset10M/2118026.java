package nl.beesting.beangenerator.generator.re;

public class ReverseGreedyExpression extends ReverseLengthRExpression {

    private static final int MAX_GREEDY_REPEAT_COUNT = 10;

    /**
	 * @param mask
	 */
    public ReverseGreedyExpression(char mask) {
        super(ReverseRExpression.GREEDY);
        switch(mask) {
            case '?':
                setStartLength(0);
                setEndLength(2);
                break;
            case '+':
                setStartLength(1);
                setEndLength(MAX_GREEDY_REPEAT_COUNT);
                break;
            case '*':
                setStartLength(0);
                setEndLength(MAX_GREEDY_REPEAT_COUNT);
                break;
            default:
                throw new IllegalArgumentException(mask + " not a valid greedy mask");
        }
    }
}
