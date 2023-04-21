package org.activebpel.rt.bpel.impl.activity.assign.from;

import org.activebpel.rt.attachment.IAeAttachmentContainer;
import org.activebpel.rt.bpel.IAeVariable;
import org.activebpel.rt.bpel.def.activity.support.AeFromDef;
import org.activebpel.rt.bpel.impl.activity.assign.AeCopyOperationComponentBase;
import org.activebpel.rt.bpel.impl.activity.assign.IAeFrom;

/**
 * Base class for objects implementing the selection of data for a &lt;from&gt; construct 
 */
public abstract class AeFromBase extends AeCopyOperationComponentBase implements IAeFrom {

    /**
    * Ctor for the base accepts def
    */
    public AeFromBase(AeFromDef aDef) {
        setVariableName(aDef.getVariable());
    }

    /**
    * No arg ctor 
    */
    protected AeFromBase() {
    }

    /**
    * Getter for the variable
    */
    protected IAeVariable getVariable() {
        return getCopyOperation().getContext().getVariable(getVariableName());
    }

    /**
    * @see org.activebpel.rt.bpel.impl.activity.assign.IAeFrom#getAttachmentsSource()
    */
    public IAeAttachmentContainer getAttachmentsSource() {
        return null;
    }
}
