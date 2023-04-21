package org.hl7.rim;

import org.hl7.types.II;
import org.hl7.types.INT;

/**<p>A structure is a container within a document. Structures have captions which can be coded. Structures can nest, and structures
   can contain entries.
</p>
<p>OpenIssue: The name of this class, and the allowable ActClass values, will be revised so as to be consistent with the ActContainer
   hierarchy, which is currently undergoing review. (November 2004)
</p>
*/
public interface ContextStructure extends Act {

    /**<p>A report identifier that remains constant across all revisions that derive from a common original. </p>
<p>An original report is the first version of a report. It gets a new unique value for <i>setId</i>, and has the value of <i>versionNumber </i>set to equal "1". 
</p>
<p>An addendum is an appendage to an existing report that contains supplemental information. The appendage is itself an original
   report. The parent report being appended is referenced via an <i>ActRelationship</i>, with <i>ActRelationship.typeCode</i> set to equal "APND" (for "appends"). The parent report being appended remains in place and its content and status are unaltered.
   
</p>
<p>A replacement report replaces an existing report. The replacement report uses the same value for <i>setId </i>as the parent report being replaced, and increments the value of <i>versionNumber </i>by 1. The state of the parent report being replaced should become "superceded", but is still retained in the system for historical
   reference.
</p>
  */
    II getSetId();

    /** Sets the property setId.
      @see #getSetId
  */
    void setSetId(II setId);

    /**<p>Version number is an integer starting at '1' and incrementing by 1. The first instance or original report should always be
   valued as '1'. The version number value must be incremented by one when a report is replaced, but can also be incremented
   more often to meet local requirements.
</p>
  */
    INT getVersionNumber();

    /** Sets the property versionNumber.
      @see #getVersionNumber
  */
    void setVersionNumber(INT versionNumber);
}
