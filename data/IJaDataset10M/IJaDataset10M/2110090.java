package org.activebpel.rt.bpel.impl.attachment;

import java.io.InputStream;
import java.util.Map;
import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.activebpel.rt.bpel.impl.IAeProcessPlan;

/**
 * Attachment storage interface.
 */
public interface IAeAttachmentStorage {

    /**
    * Associates the attachments in the given group with the given process.
    *
    * @param aGroupId
    * @param aProcessId
    */
    public void associateProcess(long aGroupId, long aProcessId) throws AeBusinessProcessException;

    /**
    * Cleans up unassociated attachments.
    * <p>
    * This can only be done at engine startup. 
    * </p>
    */
    public void cleanup() throws AeBusinessProcessException;

    /**
    * Creates a new attachment group and returns its id.
    *
    * @param aPlan
    * @return attachment group id
    */
    public long createAttachmentGroup(IAeProcessPlan aPlan) throws AeBusinessProcessException;

    /**
    * Returns the binary content of an attachment as an <code>InputStream</code>.
    * 
    * @param aAttachmentId
    * @return the binary stream
    */
    public InputStream getContent(long aAttachmentId) throws AeBusinessProcessException;

    /**
    * Returns an attachment's headers.
    *
    * @param aAttachmentId
    * @return <code>Map</code> of header name/value pairs
    */
    public Map getHeaders(long aAttachmentId) throws AeBusinessProcessException;

    /**
    * Stores an attachment (headers and content) and returns the attachment id.
    *
    * @param aGroupId
    * @param aInputStream
    * @param aHeaders
    * @return attachment id
    */
    public long storeAttachment(long aGroupId, InputStream aInputStream, Map aHeaders) throws AeBusinessProcessException;

    /**
    * Removes an attachment.
    * 
    * @param aAttachmentId
    */
    public void removeAttachment(long aAttachmentId) throws AeBusinessProcessException;
}
