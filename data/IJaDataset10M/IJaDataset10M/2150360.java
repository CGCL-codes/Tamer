package info.absu.snow;

/**
 * This exception can be thrown when no more than a single result is expected but more than one found.
 *
 * @author Denys Rtveliashvili
 */
public class MoreThanOneResult extends MatchingException {

    private static final long serialVersionUID = 1L;

    public MoreThanOneResult(String message) {
        super(message);
    }
}
