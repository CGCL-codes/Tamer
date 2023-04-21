package ca.uhn.hl7v2.model.v22.datatype;

import ca.uhn.hl7v2.model.Message;

/**
 *
 *
 * Note: The class description below has been excerpted from the Hl7 2.3.0 documentation.
 * Sectional references made below also refer to the same documentation.
 *
 * The value of such a field follows the formatting rules for a ST field except that it is
 * drawn from a site-defined (or user-defined) table of legal values. There shall be an HL7
 * table number associated with IS data types. An example of an IS field is the Event reason
 * code defined in Section 3.3.1.4, "Event reason code." This data type should be used only for
 * user-defined tables (see Section 2.7.6, "Table"). The reverse is not true, since in some
 * circumstances, it is more appropriate to use the CE data type for user-defined tables.
 * @author Neal Acharya
 */
public class IS extends ca.uhn.hl7v2.model.primitive.IS {

    /**
     * @param theMessage message to which this Type belongs
     */
    public IS(Message theMessage) {
        super(theMessage);
    }

    /**
     * @param theMessage message to which this Type belongs
     * @param theTable HL7 table from which values are to be drawn 
     */
    public IS(Message theMessage, int theTable) {
        super(theMessage, theTable);
    }

    /**
     * @param theMessage message to which this Type belongs
     * @param theTable HL7 table from which values are to be drawn 
     */
    public IS(Message theMessage, Integer theTable) {
        super(theMessage, theTable);
    }

    /**
     * @return "2.2"
     */
    public String getVersion() {
        return "2.2";
    }
}
