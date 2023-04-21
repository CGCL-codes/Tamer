package org.dicom4j.lds.server.dicom.services;

import org.dicom4j.dicom.UniqueIdentifiers.SOPClasses;
import org.dicom4j.io.media.MediaStorage;
import org.dicom4j.jlds.core.dao.DaoLayer;
import org.dicom4j.network.dimse.DimseConst;
import org.dicom4j.network.dimse.messages.DimseMessage;
import org.dicom4j.network.dimse.messages.DimseMessageFactory;
import org.dicom4j.network.dimse.messages.NActionRequest;
import org.dicom4j.network.dimse.messages.NActionResponse;
import org.dicom4j.network.dimse.services.ThreadedDimseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageCommitmentSCP extends ThreadedDimseService {

    public synchronized void doHandleMessage(byte aPresentationContextID, DimseMessage aMessage) throws Exception {
        LOGGER.info("[" + aPresentationContextID + "] - " + aMessage.getName());
        NActionRequest lRequest = (NActionRequest) aMessage;
        try {
            String lSOPClassUID = lRequest.getRequestedSOPClassUID();
            String lSOPInstanceUID = lRequest.getRequestedSOPInstanceUID();
            NActionResponse lRep = (NActionResponse) Message_Factory.newNActionResponse(lRequest.getMessageID(), lSOPClassUID);
            if (getMediaStorage().isStored(lSOPInstanceUID)) {
                lRep.setStatus(DimseConst.Status.Success);
            } else {
                lRep.setStatus(DimseConst.Status.RefusedOutOfResources);
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public MediaStorage getMediaStorage() {
        if (fMediaStorage == null) {
            throw new NullPointerException("CStoreService.getMediaStorage");
        }
        return fMediaStorage;
    }

    public void setMediaStorage(MediaStorage aMediaStorage) {
        fMediaStorage = aMediaStorage;
    }

    public void setDAO(DaoLayer aDAO) {
        fDAO = aDAO;
    }

    public DaoLayer getDAO() {
        return fDAO;
    }

    private MediaStorage fMediaStorage;

    private DaoLayer fDAO;

    private static final DimseMessageFactory Message_Factory = new DimseMessageFactory();

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageCommitmentSCP.class);
}
