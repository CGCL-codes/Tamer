package net.sourceforge.chaperon.model.pattern;

import net.sourceforge.chaperon.common.Decoder;
import net.sourceforge.chaperon.model.Violations;

/**
 * This class represents a pattern for a sequence of characters.
 *
 * @author <a href="mailto:stephan@apache.org">Stephan Michels</a>
 * @version CVS $Id: CharacterString.java,v 1.8 2004/01/18 01:36:29 benedikta Exp $
 */
public class CharacterString extends Pattern {

    private String string = "";

    /**
   * Creates a pattern for a character sequence.
   */
    public CharacterString() {
    }

    /**
   * Creates a pattern for a character sequence.
   *
   * @param string Character sequence.
   */
    public CharacterString(String string) {
        setString(string);
    }

    /**
   * Set the sequence of characters for this pattern
   *
   * @param string Character sequence
   */
    public void setString(String string) {
        this.string = string;
    }

    public void setStringAsCode(String code) {
        setString(String.valueOf((char) Integer.parseInt(code)));
    }

    /**
   * Returns the sequence of characters
   *
   * @return Seqence of characaters
   */
    public String getString() {
        return string;
    }

    public String getStringAsCode() {
        if ((string != null) && (string.length() == 1)) return String.valueOf((int) string.charAt(0));
        return null;
    }

    /**
   * Return a string representation of this pattern
   *
   * @return String representation of the pattern.
   */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Decoder.decode(string, Decoder.REGEX));
        if ((getMinOccurs() == 1) && (getMaxOccurs() == 1)) {
        } else if ((getMinOccurs() == 0) && (getMaxOccurs() == 1)) buffer.append("?"); else if ((getMinOccurs() == 0) && (getMaxOccurs() == Integer.MAX_VALUE)) buffer.append("*"); else if ((getMinOccurs() == 1) && (getMaxOccurs() == Integer.MAX_VALUE)) buffer.append("+"); else {
            buffer.append("{");
            buffer.append(String.valueOf(getMinOccurs()));
            buffer.append(",");
            buffer.append(String.valueOf(getMaxOccurs()));
            buffer.append("}");
        }
        return buffer.toString();
    }

    /**
   * Create a clone of this pattern.
   *
   * @return Clone of this pattern.
   *
   * @throws CloneNotSupportedException If an exception occurs during the cloning.
   */
    public Object clone() {
        CharacterString clone = new CharacterString();
        clone.setMinOccurs(getMinOccurs());
        clone.setMaxOccurs(getMaxOccurs());
        clone.setString(getString());
        return clone;
    }

    /**
   * Validates this pattern.
   *
   * @return Return a list of violations, if this pattern isn't valid.
   */
    public Violations validate() {
        Violations violations = new Violations();
        if ((string == null) || (string.length() <= 0)) violations.addViolation("Character string contains no characters", getLocation());
        return violations;
    }
}
