package ca.uhn.hl7v2;

/**
 * Thrown when a table value can not be found by a TableRepository. 
 * @author Bryan Tripp (bryan_tripp@sourceforge.net)
 * @deprecated should be moved to sourcegen
 */
@SuppressWarnings("serial")
public class UnknownValueException extends HL7Exception {

    public UnknownValueException(String message) {
        super(message, HL7Exception.TABLE_VALUE_NOT_FOUND);
    }
}
