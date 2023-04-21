package org.hl7.rim.decorators;

import org.hl7.rim.ContextStructure;
import org.hl7.types.II;
import org.hl7.types.INT;
import org.hl7.types.impl.IInull;
import org.hl7.types.impl.INTnull;

/** Implementation of org.hl7.rim.ContextStructure as an abstract decorator, i.e., a class that returns NULL/NA or nothing for all property accessprs amd that raises UnsupportedOperationExceptions for all mutators. This is used to adapt custom application classes to the RIM class interfaces and only bother about mapping those properties that actually apply to the application class. This can be done in one of two ways: (1) the client class can extend the decorator directly, and implement the applicable properties, or (2) the abstract decorator can be extend to a concrete decorator, which would hold a reference to the client object and method bodies to delegate and adapt the applicable properties.
 @see org.hl7.rim.ContextStructure
  */
public abstract class ContextStructureDecorator extends ActDecorator implements ContextStructure {

    /** Property accessor, returns NULL/NA if not overloaded.setId.
      @see org.hl7.rim.ContextStructure#getSetId
  */
    public II getSetId() {
        return IInull.NI;
    }

    /** Property mutator, does nothing if not overloaded.setId.
      @see org.hl7.rim.ContextStructure#setSetId
  */
    public void setSetId(II setId) {
    }

    /** Property accessor, returns NULL/NA if not overloaded.versionNumber.
      @see org.hl7.rim.ContextStructure#getVersionNumber
  */
    public INT getVersionNumber() {
        return INTnull.NI;
    }

    /** Property mutator, does nothing if not overloaded.versionNumber.
      @see org.hl7.rim.ContextStructure#setVersionNumber
  */
    public void setVersionNumber(INT versionNumber) {
    }
}
