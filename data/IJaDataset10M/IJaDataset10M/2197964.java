package org.dicom4j.network.dimse.services;

import org.dicom4j.network.dimse.DimseStatus;
import org.dicom4j.network.dimse.messages.CStoreRequestMessage;
import org.dicom4j.network.dimse.messages.CStoreResponseMessage;
import org.dicom4j.network.dimse.messages.support.AbstractDimseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class to implement a CStoreSCP service.
 * <p>
 * sub-class need to override "handleStoreRequest"
 * </p>
 * 
 * @since 0.0.2
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public abstract class CStoreSCP extends ThreadedDimseService {

    private static final Logger fLogger = LoggerFactory.getLogger(CStoreSCP.class);

    /**
	 * Create a default CStoreResponseMessage (override to make a specific default response)
	 * 
	 * @param aPresentationContextID
	 * @param aRequest
	 * @return
	 * @throws Exception
	 */
    protected CStoreResponseMessage createDefaultResponse(byte aPresentationContextID, CStoreRequestMessage aRequest) throws Exception {
        return getFactory().newCStoreResponseMessage(aRequest.getMessageID(), aRequest.getAffectedSOPClassUID().trim(), DimseStatus.Success);
    }

    @Override
    protected void doHandleMessage(byte aPresentationContextID, AbstractDimseMessage aMessage) throws Exception {
        if (aMessage instanceof CStoreRequestMessage) {
            CStoreRequestMessage lRequest = (CStoreRequestMessage) aMessage;
            handleStoreRequest(aPresentationContextID, lRequest);
        } else {
            fLogger.warn("Not a CStoreRequestMessage");
        }
    }

    public abstract void handleStoreRequest(byte aPresentationContextID, CStoreRequestMessage aRequest) throws Exception;

    @Override
    public String toString() {
        return "CStore SCP Service";
    }
}
