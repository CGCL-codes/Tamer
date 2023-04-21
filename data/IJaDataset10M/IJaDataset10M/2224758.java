package com.vangent.hieos.services.xds.registry.transactions;

import com.vangent.hieos.services.xds.registry.backend.BackendRegistry;
import com.vangent.hieos.services.xds.registry.mu.support.MetadataUpdateContext;
import com.vangent.hieos.services.xds.registry.mu.support.MetadataUpdateHelper;
import com.vangent.hieos.services.xds.registry.mu.command.MetadataUpdateCommand;
import com.vangent.hieos.services.xds.registry.mu.command.SubmitAssociationCommand;
import com.vangent.hieos.services.xds.registry.mu.command.UpdateDocumentEntryMetadataCommand;
import com.vangent.hieos.services.xds.registry.mu.command.UpdateFolderMetadataCommand;
import com.vangent.hieos.services.xds.registry.mu.command.UpdateStatusCommand;
import com.vangent.hieos.xutil.atna.ATNAAuditEvent;
import com.vangent.hieos.xutil.atna.ATNAAuditEvent.ActorType;
import com.vangent.hieos.xutil.atna.ATNAAuditEvent.IHETransaction;
import com.vangent.hieos.xutil.atna.ATNAAuditEventHelper;
import com.vangent.hieos.xutil.atna.ATNAAuditEventRegisterDocumentSet;
import com.vangent.hieos.xutil.atna.XATNALogger;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.SchemaValidationException;
import com.vangent.hieos.xutil.exception.XDSNonIdenticalHashException;
import com.vangent.hieos.xutil.exception.XDSPatientIDReconciliationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.xutil.registry.RegistryUtility;
import com.vangent.hieos.xutil.response.RegistryResponse;
import com.vangent.hieos.xutil.services.framework.XBaseTransaction;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;
import java.util.ArrayList;
import java.util.List;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Bernie Thuman
 */
public class UpdateDocumentSetRequest extends XBaseTransaction {

    MessageContext messageContext;

    private static final Logger logger = Logger.getLogger(UpdateDocumentSetRequest.class);

    /**
     *
     * @param logMessage
     * @param messageContext
     */
    public UpdateDocumentSetRequest(XLogMessage logMessage, MessageContext messageContext) {
        this.log_message = logMessage;
        this.messageContext = messageContext;
        try {
            init(new RegistryResponse(), messageContext);
        } catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
        }
    }

    /**
     *
     * @param submitObjectsRequest
     * @return
     */
    public OMElement run(OMElement submitObjectsRequest) {
        try {
            submitObjectsRequest.build();
            this.auditUpdateDocumentSetRequest(submitObjectsRequest);
            this.handleUpdateDocumentSetRequest(submitObjectsRequest);
        } catch (XdsInternalException e) {
            response.add_error(MetadataSupport.XDSRegistryError, "XDS Internal Error: " + e.getMessage(), this.getClass().getName(), log_message);
            logger.fatal(logger_exception_details(e));
        } catch (XDSPatientIDReconciliationException e) {
            response.add_error(MetadataSupport.XDSPatientIDReconciliationError, e.getMessage(), this.getClass().getName(), log_message);
        } catch (XDSNonIdenticalHashException e) {
            response.add_error(MetadataSupport.XDSNonIdenticalHash, e.getMessage(), this.getClass().getName(), log_message);
            logger.warn(logger_exception_details(e));
        } catch (SchemaValidationException e) {
            response.add_error(MetadataSupport.XDSRegistryMetadataError, "Schema Validation Errors: " + e.getMessage(), this.getClass().getName(), log_message);
        } catch (MetadataException e) {
            response.add_error(MetadataSupport.XDSRegistryMetadataError, e.getMessage(), this.getClass().getName(), log_message);
        } catch (MetadataValidationException e) {
            response.add_error(MetadataSupport.XDSRegistryMetadataError, e.getMessage(), this.getClass().getName(), log_message);
        } catch (XdsException e) {
            response.add_error(MetadataSupport.XDSRegistryError, "XDS Error: " + e.getMessage(), this.getClass().getName(), log_message);
        }
        OMElement res = null;
        try {
            res = response.getResponse();
            this.log_response();
        } catch (XdsInternalException e) {
        }
        return res;
    }

    /**
     * 
     * @param submitObjectsRequest
     * @throws XdsInternalException
     * @throws SchemaValidationException
     */
    private void handleUpdateDocumentSetRequest(OMElement submitObjectsRequest) throws XdsInternalException, SchemaValidationException, MetadataException, MetadataValidationException, XdsException {
        RegistryUtility.schema_validate_local(submitObjectsRequest, MetadataTypes.METADATA_TYPE_Rb);
        boolean commitCompleted = false;
        BackendRegistry backendRegistry = new BackendRegistry(log_message);
        try {
            MetadataUpdateContext metadataUpdateContext = new MetadataUpdateContext();
            metadataUpdateContext.setBackendRegistry(backendRegistry);
            metadataUpdateContext.setLogMessage(log_message);
            metadataUpdateContext.setRegistryResponse((RegistryResponse) response);
            metadataUpdateContext.setConfigActor(this.getConfigActor());
            Metadata m = new Metadata(submitObjectsRequest);
            MetadataUpdateHelper.logMetadata(log_message, m);
            List<MetadataUpdateCommand> muCommands = new ArrayList<MetadataUpdateCommand>();
            String submissionSetId = m.getSubmissionSetId();
            String submissionSetObjectType = m.getObjectTypeById(submissionSetId);
            System.out.println("... submissionSetObjectType = " + submissionSetObjectType);
            for (OMElement assoc : m.getAssociations()) {
                String sourceObjectId = m.getSourceObject(assoc);
                String targetObjectId = m.getTargetObject(assoc);
                System.out.println("... sourceObjectId = " + sourceObjectId);
                System.out.println("... targetObjectId = " + targetObjectId);
                MetadataUpdateCommand muCommand = null;
                if (MetadataSupport.xdsB_ihe_assoc_type_update_availability_status.equals(m.getAssocType(assoc))) {
                    muCommand = this.handleUpdateAvailabilityStatusAssociation(m, metadataUpdateContext, assoc);
                } else if (MetadataSupport.xdsB_eb_assoc_type_has_member.equals(m.getAssocType(assoc))) {
                    muCommand = this.handleHasMemberAssociation(m, metadataUpdateContext, assoc);
                } else if (MetadataSupport.xdsB_ihe_assoc_type_submit_association.equals(m.getAssocType(assoc))) {
                    muCommand = this.handleSubmitAssociation(m, metadataUpdateContext, assoc);
                } else {
                    System.out.println("Association is not a trigger!");
                }
                if (muCommand != null) {
                    muCommands.add(muCommand);
                }
            }
            boolean runStatus = false;
            for (MetadataUpdateCommand muCommand : muCommands) {
                runStatus = muCommand.run();
                if (!runStatus) {
                    break;
                }
            }
            if (runStatus) {
                backendRegistry.commit();
                commitCompleted = true;
            }
        } finally {
            if (!commitCompleted) {
                backendRegistry.rollback();
            }
        }
    }

    /**
     *
     * @param m
     * @param metadataUpdateContext
     * @param assoc
     * @return
     * @throws MetadataException
     */
    private MetadataUpdateCommand handleHasMemberAssociation(Metadata m, MetadataUpdateContext metadataUpdateContext, OMElement assoc) throws MetadataException {
        MetadataUpdateCommand muCommand = null;
        System.out.println("HasMember association found");
        String submissionSetId = m.getSubmissionSetId();
        String submissionSetObjectType = m.getObjectTypeById(submissionSetId);
        System.out.println("... submissionSetObjectType = " + submissionSetObjectType);
        String sourceObjectId = m.getSourceObject(assoc);
        String targetObjectId = m.getTargetObject(assoc);
        System.out.println("... sourceObjectId = " + sourceObjectId);
        System.out.println("... targetObjectId = " + targetObjectId);
        String targetObjectType = m.getObjectTypeById(targetObjectId);
        System.out.println("... targetObjectType = " + targetObjectType);
        OMElement targetObject = m.getObjectById(targetObjectId);
        if (sourceObjectId.equals(submissionSetId)) {
            String perviousVersion = m.getSlotValue(assoc, "PreviousVersion", 0);
            if (perviousVersion != null) {
                System.out.println("... PreviousVersion = " + perviousVersion);
                boolean associationPropagation = true;
                String associationPropagationValueText = m.getSlotValue(assoc, "AssociationPropagation", 0);
                if (associationPropagationValueText != null) {
                    if (associationPropagationValueText.equalsIgnoreCase("no")) {
                        associationPropagation = false;
                    }
                }
                System.out.println("... association propagation = " + associationPropagation);
                String lid = targetObject.getAttributeValue(MetadataSupport.lid_qname);
                System.out.println("... lid = " + lid);
                if (targetObjectType.equals("Folder")) {
                    UpdateFolderMetadataCommand updateFolderCommand = new UpdateFolderMetadataCommand(m, metadataUpdateContext);
                    updateFolderCommand.setPreviousVersion(perviousVersion);
                    updateFolderCommand.setTargetObject(targetObject);
                    updateFolderCommand.setAssociationPropagation(associationPropagation);
                    muCommand = updateFolderCommand;
                } else if (targetObjectType.equals("ExtrinsicObject")) {
                    UpdateDocumentEntryMetadataCommand updateDocumentEntryCommand = new UpdateDocumentEntryMetadataCommand(m, metadataUpdateContext);
                    updateDocumentEntryCommand.setPreviousVersion(perviousVersion);
                    updateDocumentEntryCommand.setTargetObject(targetObject);
                    updateDocumentEntryCommand.setAssociationPropagation(associationPropagation);
                    muCommand = updateDocumentEntryCommand;
                }
            }
        }
        return muCommand;
    }

    /**
     *
     * @param m
     * @param metadataUpdateContext
     * @param assoc
     * @return
     * @throws MetadataException
     */
    private MetadataUpdateCommand handleUpdateAvailabilityStatusAssociation(Metadata m, MetadataUpdateContext metadataUpdateContext, OMElement assoc) throws MetadataException {
        MetadataUpdateCommand muCommand = null;
        System.out.println("UpdateAvailabilityStatus association found");
        String targetObjectId = m.getTargetObject(assoc);
        System.out.println("... targetObjectId = " + targetObjectId);
        String newStatus = m.getSlotValue(assoc, "NewStatus", 0);
        String originalStatus = m.getSlotValue(assoc, "OriginalStatus", 0);
        System.out.println("... NewStatus = " + newStatus);
        System.out.println("... OriginalStatus = " + originalStatus);
        if (!MetadataUpdateHelper.isUUID(targetObjectId)) {
            System.out.println("*** ERROR: " + targetObjectId + " is not in UUID format");
        }
        UpdateStatusCommand updateStatusCommand = new UpdateStatusCommand(m, metadataUpdateContext);
        updateStatusCommand.setNewStatus(newStatus);
        updateStatusCommand.setOriginalStatus(originalStatus);
        updateStatusCommand.setTargetObjectId(targetObjectId);
        muCommand = updateStatusCommand;
        return muCommand;
    }

    /**
     *
     * @param m
     * @param metadataUpdateContext
     * @param assoc
     * @return
     * @throws MetadataException
     */
    private MetadataUpdateCommand handleSubmitAssociation(Metadata m, MetadataUpdateContext metadataUpdateContext, OMElement assoc) throws MetadataException {
        MetadataUpdateCommand muCommand = null;
        System.out.println("SubmitAssociation association found");
        String targetObjectId = m.getTargetObject(assoc);
        System.out.println("... targetObjectId = " + targetObjectId);
        String targetObjectType = m.getObjectTypeById(targetObjectId);
        System.out.println("... targetObjectType = " + targetObjectType);
        OMElement targetObject = m.getObjectById(targetObjectId);
        SubmitAssociationCommand submitAssociationCommand = new SubmitAssociationCommand(m, metadataUpdateContext);
        submitAssociationCommand.setTargetObject(targetObject);
        submitAssociationCommand.setSubmitAssociation(assoc);
        muCommand = submitAssociationCommand;
        return muCommand;
    }

    /**
     *
     * @param rootNode
     */
    private void auditUpdateDocumentSetRequest(OMElement rootNode) {
        try {
            XATNALogger xATNALogger = new XATNALogger();
            if (xATNALogger.isPerformAudit()) {
                ATNAAuditEventRegisterDocumentSet auditEvent = ATNAAuditEventHelper.getATNAAuditEventRegisterDocumentSet(rootNode);
                auditEvent.setActorType(ActorType.REGISTRY);
                auditEvent.setTransaction(IHETransaction.ITI57);
                auditEvent.setAuditEventType(ATNAAuditEvent.AuditEventType.IMPORT);
                xATNALogger.audit(auditEvent);
            }
        } catch (Exception ex) {
        }
    }
}
