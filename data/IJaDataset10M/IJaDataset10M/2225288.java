package ca.uhn.hl7v2.model.v231.datatype;

import ca.uhn.hl7v2.model.Message;

/**
 * 
 * Note: The class description below has been excerpted from the Hl7 2.3.1 documentation. Sectional
 * references made below also refer to the same documentation.
 *
 * Format: YYYY[MM[DD]]
 * In prior versions of HL7, this data type was always specified to be in the format YYYYMMDD. In the current and future
 * versions, the precision of a date may be expressed by limiting the number of digits used with the format specification
 * YYYY[MM[DD]]. Thus, YYYY is used to specify a precision of "year," YYYYMM specifies a precision of "month,"
 * and YYYYMMDD specifies a precision of "day."
 * By site-specific agreement, YYYYMMDD may be used where backward compatibility must be maintained.
 * Examples:   |19880704|  |199503|
 * @author Neal Acharya
 */
public class DT extends ca.uhn.hl7v2.model.primitive.DT {

    /**
     * @param theMessage message to which this Type belongs
     */
    public DT(Message theMessage) {
        super(theMessage);
    }

    /**
     * @return "2.3.1"
     */
    public String getVersion() {
        return "2.3.1";
    }
}
