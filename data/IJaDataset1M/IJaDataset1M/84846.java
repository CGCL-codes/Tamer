package org.antlr.runtime;

/** A mismatched char or Token or tree node */
public class MismatchedTokenException extends RecognitionException {

    public int expecting = Token.INVALID_TOKEN_TYPE;

    /** Used for remote debugger deserialization */
    public MismatchedTokenException() {
        ;
    }

    public MismatchedTokenException(int expecting, IntStream input) {
        super(input);
        this.expecting = expecting;
    }

    public String toString() {
        return "MismatchedTokenException(" + getUnexpectedType() + "!=" + expecting + ")";
    }
}
