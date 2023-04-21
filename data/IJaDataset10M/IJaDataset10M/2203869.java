package mcaplantlr.runtime;

public class NoViableAltException extends RecognitionException {

    public String grammarDecisionDescription;

    public int decisionNumber;

    public int stateNumber;

    /** Used for remote debugger deserialization */
    public NoViableAltException() {
        ;
    }

    public NoViableAltException(String grammarDecisionDescription, int decisionNumber, int stateNumber, IntStream input) {
        super(input);
        this.grammarDecisionDescription = grammarDecisionDescription;
        this.decisionNumber = decisionNumber;
        this.stateNumber = stateNumber;
    }

    public String toString() {
        if (input instanceof CharStream) {
            return "NoViableAltException('" + (char) getUnexpectedType() + "'@[" + grammarDecisionDescription + "])";
        } else {
            return "NoViableAltException(" + getUnexpectedType() + "@[" + grammarDecisionDescription + "])";
        }
    }
}
