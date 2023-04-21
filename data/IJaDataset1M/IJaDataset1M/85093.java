package ca.gc.drdc_rddc.atlantic.rti1516;

import hla.rti1516.AttributeAcquisitionWasNotCanceled;
import hla.rti1516.AttributeAcquisitionWasNotRequested;
import hla.rti1516.AttributeAlreadyOwned;
import hla.rti1516.AttributeDivestitureWasNotRequested;
import hla.rti1516.AttributeHandle;
import hla.rti1516.AttributeHandleSet;
import hla.rti1516.AttributeHandleValueMap;
import hla.rti1516.AttributeNotOwned;
import hla.rti1516.AttributeNotPublished;
import hla.rti1516.AttributeNotRecognized;
import hla.rti1516.AttributeNotSubscribed;
import hla.rti1516.CouldNotDiscover;
import hla.rti1516.CouldNotInitiateRestore;
import hla.rti1516.FederateAmbassador;
import hla.rti1516.FederateHandle;
import hla.rti1516.FederateHandleRestoreStatusPair;
import hla.rti1516.FederateHandleSaveStatusPair;
import hla.rti1516.FederateInternalError;
import hla.rti1516.InteractionClassHandle;
import hla.rti1516.InteractionClassNotPublished;
import hla.rti1516.InteractionClassNotRecognized;
import hla.rti1516.InteractionClassNotSubscribed;
import hla.rti1516.InteractionParameterNotRecognized;
import hla.rti1516.InvalidLogicalTime;
import hla.rti1516.JoinedFederateIsNotInTimeAdvancingState;
import hla.rti1516.LogicalTime;
import hla.rti1516.MessageRetractionHandle;
import hla.rti1516.NoRequestToEnableTimeConstrainedWasPending;
import hla.rti1516.NoRequestToEnableTimeRegulationWasPending;
import hla.rti1516.ObjectClassHandle;
import hla.rti1516.ObjectClassNotPublished;
import hla.rti1516.ObjectClassNotRecognized;
import hla.rti1516.ObjectInstanceHandle;
import hla.rti1516.ObjectInstanceNotKnown;
import hla.rti1516.OrderType;
import hla.rti1516.ParameterHandleValueMap;
import hla.rti1516.RegionHandleSet;
import hla.rti1516.RestoreFailureReason;
import hla.rti1516.SaveFailureReason;
import hla.rti1516.SpecifiedSaveLabelDoesNotExist;
import hla.rti1516.SynchronizationPointFailureReason;
import hla.rti1516.TransportationType;
import hla.rti1516.UnableToPerformSave;
import hla.rti1516.UnknownName;

public class DelegateFedAmb1516 implements FederateAmbassador {

    private hla.rti1516.FederateAmbassador fedAmb;

    public DelegateFedAmb1516(hla.rti1516.FederateAmbassador fedAmb) {
        super();
        this.fedAmb = fedAmb;
    }

    public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel) throws FederateInternalError {
    }

    public void synchronizationPointRegistrationFailed(String synchronizationPointLabel, SynchronizationPointFailureReason reason) throws FederateInternalError {
    }

    public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag) throws FederateInternalError {
    }

    public void federationSynchronized(String synchronizationPointLabel) throws FederateInternalError {
    }

    public void initiateFederateSave(String label) throws UnableToPerformSave, FederateInternalError {
    }

    public void initiateFederateSave(String label, LogicalTime time) throws InvalidLogicalTime, UnableToPerformSave, FederateInternalError {
    }

    public void federationSaved() throws FederateInternalError {
    }

    public void federationNotSaved(SaveFailureReason reason) throws FederateInternalError {
    }

    public void federationSaveStatusResponse(FederateHandleSaveStatusPair[] response) throws FederateInternalError {
    }

    public void requestFederationRestoreSucceeded(String label) throws FederateInternalError {
    }

    public void requestFederationRestoreFailed(String label) throws FederateInternalError {
    }

    public void federationRestoreBegun() throws FederateInternalError {
    }

    public void initiateFederateRestore(String label, FederateHandle federateHandle) throws SpecifiedSaveLabelDoesNotExist, CouldNotInitiateRestore, FederateInternalError {
    }

    public void federationRestored() throws FederateInternalError {
    }

    public void federationNotRestored(RestoreFailureReason reason) throws FederateInternalError {
    }

    public void federationRestoreStatusResponse(FederateHandleRestoreStatusPair[] response) throws FederateInternalError {
    }

    public void startRegistrationForObjectClass(ObjectClassHandle theClass) throws ObjectClassNotPublished, FederateInternalError {
    }

    public void stopRegistrationForObjectClass(ObjectClassHandle theClass) throws ObjectClassNotPublished, FederateInternalError {
    }

    public void turnInteractionsOn(InteractionClassHandle theHandle) throws InteractionClassNotPublished, FederateInternalError {
    }

    public void turnInteractionsOff(InteractionClassHandle theHandle) throws InteractionClassNotPublished, FederateInternalError {
    }

    public void objectInstanceNameReservationSucceeded(String objectName) throws UnknownName, FederateInternalError {
    }

    public void objectInstanceNameReservationFailed(String objectName) throws UnknownName, FederateInternalError {
    }

    public void discoverObjectInstance(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass, String objectName) throws CouldNotDiscover, ObjectClassNotRecognized, FederateInternalError {
    }

    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotSubscribed, FederateInternalError {
    }

    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, RegionHandleSet sentRegions) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotSubscribed, FederateInternalError {
    }

    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, LogicalTime theTime, OrderType receivedOrdering) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotSubscribed, FederateInternalError {
    }

    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, LogicalTime theTime, OrderType receivedOrdering, RegionHandleSet sentRegions) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotSubscribed, FederateInternalError {
    }

    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotSubscribed, InvalidLogicalTime, FederateInternalError {
    }

    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, RegionHandleSet sentRegions) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotSubscribed, InvalidLogicalTime, FederateInternalError {
    }

    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport) throws InteractionClassNotRecognized, InteractionParameterNotRecognized, InteractionClassNotSubscribed, FederateInternalError {
    }

    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, RegionHandleSet sentRegions) throws InteractionClassNotRecognized, InteractionParameterNotRecognized, InteractionClassNotSubscribed, FederateInternalError {
    }

    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, LogicalTime theTime, OrderType receivedOrdering) throws InteractionClassNotRecognized, InteractionParameterNotRecognized, InteractionClassNotSubscribed, FederateInternalError {
    }

    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, LogicalTime theTime, OrderType receivedOrdering, RegionHandleSet regions) throws InteractionClassNotRecognized, InteractionParameterNotRecognized, InteractionClassNotSubscribed, FederateInternalError {
    }

    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle messageRetractionHandle) throws InteractionClassNotRecognized, InteractionParameterNotRecognized, InteractionClassNotSubscribed, InvalidLogicalTime, FederateInternalError {
    }

    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle messageRetractionHandle, RegionHandleSet sentRegions) throws InteractionClassNotRecognized, InteractionParameterNotRecognized, InteractionClassNotSubscribed, InvalidLogicalTime, FederateInternalError {
    }

    public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering) throws ObjectInstanceNotKnown, FederateInternalError {
    }

    public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering, LogicalTime theTime, OrderType receivedOrdering) throws ObjectInstanceNotKnown, FederateInternalError {
    }

    public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle) throws ObjectInstanceNotKnown, InvalidLogicalTime, FederateInternalError {
    }

    public void attributesInScope(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotSubscribed, FederateInternalError {
    }

    public void attributesOutOfScope(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotSubscribed, FederateInternalError {
    }

    public void provideAttributeValueUpdate(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, byte[] userSuppliedTag) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotOwned, FederateInternalError {
    }

    public void turnUpdatesOnForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotOwned, FederateInternalError {
    }

    public void turnUpdatesOffForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotOwned, FederateInternalError {
    }

    public void requestAttributeOwnershipAssumption(ObjectInstanceHandle theObject, AttributeHandleSet offeredAttributes, byte[] userSuppliedTag) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeAlreadyOwned, AttributeNotPublished, FederateInternalError {
    }

    public void requestDivestitureConfirmation(ObjectInstanceHandle theObject, AttributeHandleSet offeredAttributes) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotOwned, AttributeDivestitureWasNotRequested, FederateInternalError {
    }

    public void attributeOwnershipAcquisitionNotification(ObjectInstanceHandle theObject, AttributeHandleSet securedAttributes, byte[] userSuppliedTag) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeAcquisitionWasNotRequested, AttributeAlreadyOwned, AttributeNotPublished, FederateInternalError {
    }

    public void attributeOwnershipUnavailable(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeAlreadyOwned, AttributeAcquisitionWasNotRequested, FederateInternalError {
    }

    public void requestAttributeOwnershipRelease(ObjectInstanceHandle theObject, AttributeHandleSet candidateAttributes, byte[] userSuppliedTag) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeNotOwned, FederateInternalError {
    }

    public void confirmAttributeOwnershipAcquisitionCancellation(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws ObjectInstanceNotKnown, AttributeNotRecognized, AttributeAlreadyOwned, AttributeAcquisitionWasNotCanceled, FederateInternalError {
    }

    public void informAttributeOwnership(ObjectInstanceHandle theObject, AttributeHandle theAttribute, FederateHandle theOwner) throws ObjectInstanceNotKnown, AttributeNotRecognized, FederateInternalError {
    }

    public void attributeIsNotOwned(ObjectInstanceHandle theObject, AttributeHandle theAttribute) throws ObjectInstanceNotKnown, AttributeNotRecognized, FederateInternalError {
    }

    public void attributeIsOwnedByRTI(ObjectInstanceHandle theObject, AttributeHandle theAttribute) throws ObjectInstanceNotKnown, AttributeNotRecognized, FederateInternalError {
    }

    public void timeRegulationEnabled(LogicalTime time) throws InvalidLogicalTime, NoRequestToEnableTimeRegulationWasPending, FederateInternalError {
    }

    public void timeConstrainedEnabled(LogicalTime time) throws InvalidLogicalTime, NoRequestToEnableTimeConstrainedWasPending, FederateInternalError {
    }

    public void timeAdvanceGrant(LogicalTime theTime) throws InvalidLogicalTime, JoinedFederateIsNotInTimeAdvancingState, FederateInternalError {
    }

    public void requestRetraction(MessageRetractionHandle theHandle) throws FederateInternalError {
    }
}
