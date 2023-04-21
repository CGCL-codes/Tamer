package org.dicom4j.lds.server.dicom.services;

import org.dicom4j.data.CommandSet;
import org.dicom4j.data.DataSet;
import org.dicom4j.dicom.DicomTags;
import org.dicom4j.dicom.TransferSyntaxes;
import org.dicom4j.dicom.UniqueIdentifiers.SOPClasses;
import org.dicom4j.io.media.MediaStorage;
import org.dicom4j.io.media.handlers.MediaStoreHandler;
import org.dicom4j.jlds.core.beans.Instance;
import org.dicom4j.network.association.associate.AssociateAbort;
import org.dicom4j.network.dimse.DimseConst;
import org.dicom4j.network.dimse.messages.CStoreRequestMessage;
import org.dicom4j.network.dimse.messages.CStoreResponseMessage;
import org.dicom4j.network.dimse.messages.DimseMessageFactory;
import org.dicom4j.network.dimse.services.CStoreSCP;
import org.dicom4jserver.business.BusinessLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageCStoreSCP extends CStoreSCP {

    /**
	 * the logger
	 */
    private static final Logger fLogger = LoggerFactory.getLogger(ImageCStoreSCP.class);

    private BusinessLayer fBusinessLayer;

    private static final DimseMessageFactory Message_Factory = new DimseMessageFactory();

    private StoreRequest fStoreRequest;

    private Instance fImageBean;

    public ImageCStoreSCP() {
        super();
    }

    public void handleStoreRequest(byte aPresentationContextID, CStoreRequestMessage aRequest) throws Exception {
        fLogger.info("[" + aPresentationContextID + "] - " + aRequest.getName() + "(" + SOPClasses.toString(aRequest.getAffectedSOPClassUID().trim()) + " SOPClass, AET: " + aRequest.getMoveOriginatorAETasString() + ")");
        fStoreRequest = new StoreRequest(aPresentationContextID, aRequest.getMessageID());
        fStoreRequest.readRequest(aRequest);
        try {
            fStoreRequest.readDataSet(aRequest.getDataSet());
            fStoreRequest.checkRequest();
            if (getBusinessLayer().isInstanceStored(fStoreRequest.getAffectedSOPInstanceUID())) {
                fLogger.info("instance " + fStoreRequest.getAffectedSOPInstanceUID() + "already stored");
                CStoreResponseMessage lRep = new CStoreResponseMessage(aRequest.getMessageID(), aRequest.getAffectedSOPClassUID().trim(), CommandSet.NO_DATASET, DimseConst.Status.Success);
                lRep.setAffectedSOPInstanceUID(fStoreRequest.getAffectedSOPInstanceUID());
                sendMessageToRemotePeer(aPresentationContextID, lRep);
                setFinished();
            }
            fImageBean = getBusinessLayer().getInstanceOrCreateNew(aRequest.getDataSet());
            if (fImageBean == null) throw new ImageCStoreSCPException("can get an Instance Bean");
            getBusinessLayer().getFileMediaStorage().storeDataSet(aRequest, TransferSyntaxes.Default, new OnStoreHandler());
        } catch (Throwable e) {
            fLogger.error("[" + e.getClass().getName() + "] " + e.getMessage());
            sendErrorResponse(e.getClass().getName());
        }
    }

    private class OnStoreHandler implements MediaStoreHandler {

        public void onStored(MediaStorage mediaStorage, CStoreResponseMessage aStoreResponse) {
            fLogger.debug("onStored : " + aStoreResponse);
            if (!aStoreResponse.isSuccess()) {
                try {
                    fLogger.warn(aStoreResponse.getAffectedSOPInstanceUID() + "Not Stored");
                    getBusinessLayer().getInstanceBusiness().deleteInstance(fImageBean);
                    sendMessageToRemotePeer(fStoreRequest.getPresID(), aStoreResponse);
                    setFinished();
                } catch (Exception e) {
                    fLogger.error(e.getMessage());
                    sendErrorResponse("");
                }
            } else {
                try {
                    sendMessageToRemotePeer(fStoreRequest.getPresID(), aStoreResponse);
                } catch (Exception e) {
                    fLogger.error(e.getMessage());
                }
                setFinished();
            }
        }
    }

    private void sendErrorResponse(String aErrorComment) {
        try {
            CStoreResponseMessage lRep = new CStoreResponseMessage(fStoreRequest.fMessageID, fStoreRequest.getAffectedSOPClassUID(), fStoreRequest.getAffectedSOPInstanceUID());
            lRep.setStatus(DimseConst.Status.RefusedOutOfResources);
            lRep.getErrorCommentElement().setValue(aErrorComment);
            sendMessageToRemotePeer(fStoreRequest.getPresID(), lRep);
            setFinished();
        } catch (Exception e) {
            sendSafeAbortToRemotePeer(AssociateAbort.ServiceUserAbort);
        }
    }

    public BusinessLayer getBusinessLayer() {
        return fBusinessLayer;
    }

    public void setBusinessLayer(BusinessLayer businessLayer) {
        fBusinessLayer = businessLayer;
    }

    private class StoreRequest {

        private byte fPresID;

        private int fMessageID;

        private String fAffectedSOPClassUID = "";

        private String fAffectedSOPInstanceUID = "";

        private String fStudyInstanceUID = "";

        private String fSeriesInstanceUID = "";

        private String fSOPInstanceUID = "";

        public StoreRequest(byte aPresentationID, int aMessageID) {
            fPresID = aPresentationID;
            fMessageID = aMessageID;
        }

        private void readRequest(CStoreRequestMessage aRequest) {
            fAffectedSOPClassUID = aRequest.getAffectedSOPClassUID();
            fLogger.info("AffectedSOPClassUID: " + fAffectedSOPClassUID);
            fAffectedSOPInstanceUID = aRequest.getAffectedSOPInstanceUID();
            fLogger.info("AffectedSOPInstanceUID: " + fAffectedSOPInstanceUID);
        }

        public void checkRequest() throws ImageCStoreSCPException {
            if (fAffectedSOPInstanceUID.equals("")) {
                throw new ImageCStoreSCPException("AffectedSOPInstanceUID is mandatory and must be in the request");
            }
        }

        public byte getPresID() {
            return fPresID;
        }

        public int getMessageID() {
            return fMessageID;
        }

        public String getAffectedSOPClassUID() {
            return fAffectedSOPClassUID;
        }

        public String getAffectedSOPInstanceUID() {
            return fAffectedSOPInstanceUID;
        }

        public String getStudyInstanceUID() {
            return fStudyInstanceUID;
        }

        public String getSeriesInstanceUID() {
            return fSeriesInstanceUID;
        }

        public String getSOPInstanceUID() {
            return fSOPInstanceUID;
        }

        public void readDataSet(DataSet aDataSet) throws ImageCStoreSCPException {
            try {
                if (aDataSet == null) throw new ImageCStoreSCPException("No DataSet in the request");
                if (aDataSet.hasElement(DicomTags.SOPInstanceUID)) {
                    fSOPInstanceUID = aDataSet.getUniqueIdentifier(DicomTags.SOPInstanceUID).getSingleStringValue().trim();
                } else {
                    throw new Exception("No SOPInstanceUID in DataSet");
                }
                if (aDataSet.hasElement(DicomTags.SeriesInstanceUID)) {
                    fSeriesInstanceUID = aDataSet.getUniqueIdentifier(DicomTags.SeriesInstanceUID).getSingleStringValue().trim();
                } else {
                    throw new Exception("No SeriesInstanceUID in DataSet");
                }
                if (aDataSet.hasElement(DicomTags.StudyInstanceUID)) {
                    fStudyInstanceUID = aDataSet.getUniqueIdentifier(DicomTags.StudyInstanceUID).getSingleStringValue().trim();
                } else {
                    throw new Exception("No StudyInstanceUID in DataSet");
                }
            } catch (Exception e) {
                throw new ImageCStoreSCPException(e);
            }
        }
    }
}
