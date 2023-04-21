package reducedmodel;

/**
 * Represents a segment defined by a beginning and end marker that hides the behavior of smaller segments it contains.
 * A HidingMatchedBreaker always forms in consideration of which start string comes first.
 * Ex: " " " always matches the first quotation mark with the second even if the second and third were in existance first
 */
public class HidingBreaker extends MatchedBreaker {

    public HidingBreaker(int length, UnmatchedBreaker start, UnmatchedBreaker end) {
        super(length, start, end);
    }

    public Object execute(BreakerVisitor visitor, Object... args) {
        return visitor.hidingCase(this, args);
    }
}
