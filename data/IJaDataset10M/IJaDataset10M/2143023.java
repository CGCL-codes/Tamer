package org.dicom4jserver.services;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.dicom4j.data.CommandSet;
import org.dicom4j.data.DataSet;
import org.dicom4j.dicom.UniqueIdentifiers.TransferSyntaxes;
import org.dicom4j.io.media.MediaStorage;
import org.dicom4j.network.association.Association;
import org.dicom4j.network.association.AssociationConfiguration;
import org.dicom4j.network.association.AssociationConnector;
import org.dicom4j.network.association.AssociationException;
import org.dicom4j.network.association.associate.AssociateAbort;
import org.dicom4j.network.association.associate.AssociateRequest;
import org.dicom4j.network.association.associate.AssociateSession;
import org.dicom4j.network.association.listeners.defaults.DefaultAssociationListener;
import org.dicom4j.network.association.support.AssociationConfigurationImpl;
import org.dicom4j.network.association.support.AssociationConnectorImpl;
import org.dicom4j.network.dimse.DimseConst;
import org.dicom4j.network.dimse.messages.CMoveRequestMessage;
import org.dicom4j.network.dimse.messages.CMoveResponseMessage;
import org.dicom4j.network.dimse.messages.CStoreRequestMessage;
import org.dicom4j.network.dimse.messages.CStoreResponseMessage;
import org.dicom4j.network.dimse.messages.DimseMessage;
import org.dicom4j.network.dimse.messages.DimseMessageFactory;
import org.dicom4j.toolkit.beans.InstanceBean;
import org.dicom4j.tools.commons.beans.DicomNodeBean;
import org.dolmen.core.lang.thread.ThreadUtils;
import org.dolmen.core.lang.thread.Worker;
import org.dolmen.core.lang.thread.WorkerHandler;
import org.dolmen.network.transport.connector.TransportConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstanceMoveSCU extends DefaultAssociationListener {

    public InstanceMoveSCU(CMoveContext aContext) {
        fContext = aContext;
    }

    public void sendInstance(List aInstance, DicomNodeBean aPeer) {
        fLogger.info("we must send: " + aInstance.size() + " instances");
        fInstancesToSend = aInstance;
        fNumberOfCompletedSubOperations = 0;
        fNumberOfFailedSubOperations = 0;
        fNumberOfRemainingSubOperations = fInstancesToSend.size();
        fNumberOfWarningSubOperations = 0;
        TreeSet lSopClasses = getSOPClassesToSend();
        fAssociateRequest = new AssociateRequest(fContext.getMoveOriginatorAssoc().getAssociateSession().getCalledAET(), fContext.getCMoveRequestMessage().getMoveDestination());
        Iterator lIT = lSopClasses.iterator();
        while (lIT.hasNext()) {
            String SOPUID = (String) lIT.next();
            fAssociateRequest.addPresentationContext(SOPUID, TransferSyntaxes.Default);
        }
        AssociationConfiguration lConfig = new AssociationConfigurationImpl();
        fLogger.info("config");
        lConfig.setAssociationListener(this);
        AssociationConnector lConnector = new AssociationConnectorImpl();
        lConnector.setTransportConnector(new TransportConnector());
        lConnector.setConfiguration(lConfig);
        fAssociation = null;
        try {
            lConnector.connect(aPeer.getHostName(), aPeer.getPort(), fAssociateRequest);
            fWorker = new SendWorker();
            ThreadUtils.startWorker(fWorker, fWorkerHandler, null);
        } catch (Exception e) {
            notifyException(e);
        }
    }

    private void notifyException(Exception ex) {
        if (fWorkerHandler != null) {
            fWorkerHandler.onException(ex);
        } else {
            fLogger.warn("No ExceptionListener to handle " + ex.getMessage() + " exception");
        }
    }

    private class SendWorker implements Worker {

        public SendWorker() {
        }

        public Object doWork(Object aData) throws Exception {
            fLogger.debug("doWork");
            fisAllSended = false;
            while (!fisAllSended) {
                Thread.sleep(2000);
                fLogger.debug("AA");
                if (fAssociation != null) {
                    for (int j = 0; j < fInstancesToSend.size(); j++) {
                        fLogger.debug("BB:" + j);
                        InstanceBean lInstance = (InstanceBean) fInstancesToSend.get(j);
                        String lUID = lInstance.getInstanceUID();
                        fLogger.debug("InstanceUID: " + lUID);
                        String lSop = lInstance.getSOPClassUID();
                        fLogger.debug("SOPClassUID: " + lSop);
                        byte lPres = fAssociateRequest.getPresentationContextId(lSop);
                        if (!fAssociation.getAssociateSession().isAccepted(lPres)) {
                            fLogger.debug("SOPClass " + lSop + " not supported");
                            fNumberOfFailedSubOperations++;
                            decreaseNumberOfRemainingSubOperations();
                            sendResponseToMoveOriginator();
                        } else {
                            fLogger.debug("we must send " + lUID + " instance");
                            try {
                                CStoreRequestMessage lStoreMSG = createCStoreRequestMessage(lSop, lUID);
                                fLogger.debug("sendMessage: " + lStoreMSG.getName());
                                if (fAssociation.isReleased()) {
                                    fLogger.warn("fAssociation.isReleased");
                                }
                                fAssociation.sendMessage(lPres, lStoreMSG);
                                if (fInstancesToSend.size() == j + 1) {
                                    fisAllSended = true;
                                }
                            } catch (Exception e) {
                                fLogger.error(e.getMessage());
                                decreaseNumberOfRemainingSubOperations();
                                fNumberOfFailedSubOperations++;
                                if (fNumberOfRemainingSubOperations == 0) {
                                    fLogger.debug("sendMessageToRemotePeer in catch");
                                    fLogger.debug("j: " + j);
                                    CMoveResponseMessage lMoveRep = createDefaultResponse(fContext.getCMoveRequestMessage());
                                    fLogger.debug("trying to send " + lMoveRep.getName() + " to " + fContext.getMoveOriginatorAssoc().toString());
                                    if (fContext.getMoveOriginatorAssoc().isEstablished()) {
                                        getMoveOriginator().sendMessage(fContext.getAPresentationContextID(), lMoveRep);
                                        fLogger.debug(lMoveRep.getName() + " sended");
                                    }
                                    fisAllSended = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            fLogger.debug("work completed");
            return null;
        }

        public boolean isAllSended() {
            return fisAllSended;
        }

        private boolean fisAllSended;
    }

    public void messageReceived(Association aAssociation, byte aPresentationContextID, DimseMessage aMessage) throws Exception {
        fLogger.info("messageReceived: " + aMessage.getName());
        if (aMessage instanceof CStoreResponseMessage) {
            CStoreResponseMessage lRep = (CStoreResponseMessage) aMessage;
            if (lRep.isSuccess()) {
                fNumberOfCompletedSubOperations++;
            } else {
                fNumberOfFailedSubOperations++;
            }
            decreaseNumberOfRemainingSubOperations();
            CMoveResponseMessage lMoveRep = createDefaultResponse(fContext.getCMoveRequestMessage());
            if (fContext.getMoveOriginatorAssoc().isEstablished()) {
                fContext.getMoveOriginatorAssoc().sendMessage(aPresentationContextID, lMoveRep);
                fLogger.debug(lMoveRep.getName() + " sended");
            }
        }
    }

    public void associationOpened(Association aAssociation, AssociateSession aAssociateSession) throws Exception {
        fLogger.debug("associationOpened");
        fAssociation = aAssociation;
    }

    public void associationReleased(Association aAssociation) throws Exception {
        fLogger.debug("associationReleased");
        if (!fWorker.isAllSended()) {
            fLogger.warn("associationReleased but some instances was sent");
            notifyException(new AssociationException("Association Released"));
        }
    }

    public void associationAborted(Association aAssociation, AssociateAbort aAbort) throws Exception {
        fLogger.warn("associationAborted");
    }

    public void exceptionCaught(Association aAssociation, Throwable cause) {
        try {
            fLogger.error(cause.getMessage());
            aAssociation.sendAbort(AssociateAbort.ServiceUserAbort);
            notifyException(new Exception(cause));
        } catch (Exception e) {
            fLogger.warn(e.getMessage());
        }
    }

    private TreeSet getSOPClassesToSend() {
        TreeSet lSopClasses = new TreeSet();
        for (int i = 0; i < fInstancesToSend.size(); i++) {
            InstanceBean lInstance = (InstanceBean) fInstancesToSend.get(i);
            lSopClasses.add(lInstance.getSOPClassUID());
        }
        fLogger.info(lSopClasses.size() + " SOPClasse(s) found");
        return lSopClasses;
    }

    private CStoreRequestMessage createCStoreRequestMessage(String aSOPClass, String aSopInstance) throws Exception {
        CStoreRequestMessage lStoreMSG = (CStoreRequestMessage) fMessageFactory.newCStoreRequest(aSOPClass);
        lStoreMSG.setAffectedSOPInstanceUID(aSopInstance);
        fLogger.debug("trying to retrieve data from Media");
        DataSet lData = fMediaStorage.retrieveDataSet(aSopInstance);
        fLogger.debug("data retrieved");
        lStoreMSG.setDataSet(lData);
        fLogger.debug("DataSet set to Message");
        return lStoreMSG;
    }

    public void sendResponseToMoveOriginator() throws Exception {
        CMoveResponseMessage lMoveRep = createDefaultResponse(fContext.getCMoveRequestMessage());
        Association lAssoc = fContext.getMoveOriginatorAssoc();
        fLogger.debug("trying to send " + lMoveRep.getName() + " to " + lAssoc.toString());
        if (lAssoc.isEstablished()) {
            lAssoc.sendMessage(fContext.getAPresentationContextID(), lMoveRep);
            fLogger.debug(lMoveRep.getName() + " sended");
        }
    }

    protected CMoveResponseMessage createDefaultResponse(CMoveRequestMessage aRequest) throws Exception {
        CMoveResponseMessage lMoveRep = new CMoveResponseMessage(aRequest.getMessageID(), aRequest.getAffectedSOPClassUID().trim(), CommandSet.NO_DATASET, DimseConst.Status.Success);
        lMoveRep.setStatus(DimseConst.Status.Pending);
        lMoveRep.setNumberOfCompletedSubOperations(fNumberOfCompletedSubOperations);
        lMoveRep.setNumberOfFailedSubOperations(fNumberOfFailedSubOperations);
        lMoveRep.setNumberOfRemainingSubOperations(fNumberOfRemainingSubOperations);
        lMoveRep.setNumberOfWarningSubOperations(fNumberOfWarningSubOperations);
        if (fNumberOfRemainingSubOperations == 0 && fNumberOfFailedSubOperations == 0) {
            lMoveRep.setStatus(DimseConst.Status.Success);
        }
        if (fNumberOfRemainingSubOperations == 0 && fNumberOfFailedSubOperations != 0) {
            lMoveRep.setStatus(DimseConst.Status.UnableToPerformSubOperations);
        }
        return lMoveRep;
    }

    private void decreaseNumberOfRemainingSubOperations() {
        fNumberOfRemainingSubOperations--;
        fLogger.debug("decreaseNumberOfRemainingSubOperations: " + fNumberOfRemainingSubOperations);
    }

    private Association getMoveOriginator() {
        return fContext.getMoveOriginatorAssoc();
    }

    public void setMediaStorage(MediaStorage aMediaStorage) {
        fMediaStorage = aMediaStorage;
    }

    public void setWorkerHandler(WorkerHandler aWorkerHandler) {
        fWorkerHandler = aWorkerHandler;
    }

    public MediaStorage fMediaStorage;

    public DicomNodeBean fDicomNodeBean;

    public List fInstancesToSend;

    private int fNumberOfRemainingSubOperations;

    private int fNumberOfCompletedSubOperations;

    private int fNumberOfFailedSubOperations;

    private int fNumberOfWarningSubOperations;

    private CMoveContext fContext;

    private WorkerHandler fWorkerHandler;

    private Association fAssociation;

    private AssociateRequest fAssociateRequest;

    private SendWorker fWorker;

    private static final DimseMessageFactory fMessageFactory = new DimseMessageFactory();

    private static final Logger fLogger = LoggerFactory.getLogger(InstanceMoveSCU.class);
}
