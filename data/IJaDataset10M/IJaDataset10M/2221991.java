package cz.cuni.mff.ufal.volk.patterns;

/**
 * Exception thrown by the {@link Pattern#evaluate(String...)} method
 * if the pattern evaluation has failed.
 *
 * @author Bartłomiej Etenkowski
 */
public class PatternEvaluationException extends RuntimeException {

    private static final long serialVersionUID = 8863102796650014895L;

    public PatternEvaluationException() {
    }

    public PatternEvaluationException(String message) {
        super(message);
    }

    public PatternEvaluationException(Throwable cause) {
        super(cause);
    }

    public PatternEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
