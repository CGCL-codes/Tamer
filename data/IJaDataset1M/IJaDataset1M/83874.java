package org.hl7.rim;

import org.hl7.types.ANY;
import org.hl7.types.LIST;

/**<p>An activity of an automated system.</p>
<p><i>Discussion:</i>Such activities are invoked either by an outside command or are scheduled and executed spontaneously by the device (e.g.,
   regular calibration or flushing). The command to execute the task has moodCode &lt;= ORD; an executed task (including a task
   in progress) has moodCode &lt;= EVN, an automatic task on the schedule has moodCode &lt;= APT.
</p>
*/
public interface DeviceTask extends Act {

    /**<p>The parameters of the task submitted to the device upon the issuance of a command (or configuring the schedule of spontaneously
   executed tasks).
</p>
<p><i>Rationale:</i> Some parameters for tasks are uniquely defined by a specific model of equipment. Most critical arguments of a task (e.g.,
   container to operate on, positioning, timing, etc.) are specified in an HL7 standardized structure, and the parameter list
   would not be used for those. The parameter list is used only for those parameters that cannot be standardized because they
   are uniquely defined for a specific model of equipment. NOTE: This means that the semantics and interpretation of a parameterValue
   can <b>only</b> be made with an understanding of the specifications or documentation for the specific device being addressed. This information
   is not conveyed as part of the message.
</p>
<p><i>Constraints:</i> Parameters are only specified here if they are not included in a separate HL7 defined structure. The parameters are a list
   of any data values interpreted by the device. The parameters should be typed with an appropriate HL7 data type (e.g., codes
   for nominal settings, such as flags, REAL and INT for numbers, TS for points in time, PQ for dimensioned quantities, etc.).
   However, besides this HL7 data typing, the functioning of the parameters is opaque to the HL7 standardization.
</p>
  */
    LIST<ANY> getParameterValue();

    /** Sets the property parameterValue.
      @see #getParameterValue
  */
    void setParameterValue(LIST<ANY> parameterValue);
}
