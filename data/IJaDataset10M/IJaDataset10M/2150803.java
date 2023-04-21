package hailmary.dicebags;

/**
 * An object encapsulating a dice roll or its result.
 * Its prefix in <code>Connection</code> should be <code>dice</code>.
 * @see hailmary.network.Connection#sendData(String,Object)
 * @author Corvass
 */
public class DiceMessage {

    private boolean isRoll;

    private String dice;

    /**
   * Constructs a new dice message.
   * @param isRoll <code>true</code> if the message encodes a roll,
   *               <code>false</code> if it encodes a result.
   * @param dice a string representing the roll or the result
   */
    public DiceMessage(boolean isRoll, String dice) {
        this.isRoll = isRoll;
        this.dice = dice;
    }

    /**
   * Constructs a new dice message from a string.
   * @param s a string representing a dice message
   */
    public DiceMessage(String s) {
        if (s.substring(0, 6).equalsIgnoreCase("rolls ")) {
            isRoll = true;
            dice = s.substring(6);
        } else {
            isRoll = false;
            dice = s.substring(7);
        }
    }

    /**
   * Represents this object as a string. The following
   * format is used:
   * <blockquote>
   * {rolls|rolled} &lt;dice&gt;
   * </blockquote>
   * Rolls are prefixed with "rolls", and results are
   * prefixed with "rolled".
   * @return a string representing this dice message
   */
    public String toString() {
        return (isRoll ? "rolls " : "rolled ") + dice;
    }
}
