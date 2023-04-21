package org.activebpel.rt.bpel.impl.activity.assign;

import org.activebpel.rt.attachment.IAeAttachmentContainer;
import org.activebpel.rt.bpel.AeBusinessProcessException;

/**
 * Interface for an impl object that knows how to extract data from an AeFromDef 
 */
public interface IAeFrom {

    /**
    * Extracts the data for the copy operation based on the info in the &lt;from&gt; definition
    * @throws AeBusinessProcessException
    */
    public Object getFromData() throws AeBusinessProcessException;

    /**
    * Setter for the copy operation that is the parent of this &lt;from&gt;
    * @param aCopyOperation
    */
    public void setCopyOperation(IAeCopyOperation aCopyOperation);

    /**
    * Returns the attachment container associated with the from data
    */
    public IAeAttachmentContainer getAttachmentsSource();
}
