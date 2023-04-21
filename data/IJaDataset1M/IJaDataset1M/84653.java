package org.hl7.rim;

import org.hl7.types.CE;
import org.hl7.types.CS;
import org.hl7.types.ED;
import org.hl7.types.SET;
import org.hl7.types.ST;

/**<p>A message that provides information about the communication, parsing or non-business-rule validation of the message being
   acknowledged.
</p>
*/
public interface AcknowledgementDetail extends InfrastructureRoot {

    /**<p>Identifies the kind of information specified in the acknowledgement message. Options are: Error, Warning or Information.</p>
  */
    CS getTypeCode();

    /** Sets the property typeCode.
      @see #getTypeCode
  */
    void setTypeCode(CS typeCode);

    /**<p>A code identifying the specific message to be provided.</p>
<p>Discussion: A textual value may be specified as the print name, or for non-coded messages, as the original text.</p>
<p>Examples: 'Required attribute xxx is missing', 'System will be unavailable March 19 from 0100 to 0300'</p>
  */
    CE getCode();

    /** Sets the property code.
      @see #getCode
  */
    void setCode(CE code);

    /**<p>Identifies additional diagnostic information relevant to the message.</p>
<p>Discussion: This may be free text or structured data (e.g. XML).</p>
<p>Examples: Java exception, memory dump, internal error code, call-stack information, etc.</p>
  */
    ED getText();

    /** Sets the property text.
      @see #getText
  */
    void setText(ED text);

    /**<p>Identifies a position within the message being acknowledged that is related to the message.</p>
<p>Discussion: Not all messages will have an associated location. Some messages may relate to multiple locations.</p>
<p>Example: There is no location for a missing element, and there may be two locations if two elements violate a conditionality
   constraint.
</p>
<p>OpenIssue: The specific format for the string that defines the message location needs to be identified. This might be xPath
   or possibly OCL
</p>
  */
    SET<ST> getLocation();

    /** Sets the property location.
      @see #getLocation
  */
    void setLocation(SET<ST> location);

    /**
  */
    Acknowledgement getAcknowledgement();

    /** Sets the property acknowledgement.
      @see #getAcknowledgement
  */
    void setAcknowledgement(Acknowledgement acknowledgement);
}
