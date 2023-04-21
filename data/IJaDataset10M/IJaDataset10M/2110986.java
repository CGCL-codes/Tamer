package net.sourceforge.hlagile.Bridge.Plugins.HLA;

import java.util.Set;
import net.sourceforge.hlagile.Bridge.Bridge;
import net.sourceforge.hlagile.Bridge.OnRamp;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.FederateHandleSaveStatusPair;
import hla.rti1516e.FederateHandleSet;
import hla.rti1516e.FederateRestoreStatus;
import hla.rti1516e.FederationExecutionInformationSet;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.RestoreFailureReason;
import hla.rti1516e.SaveFailureReason;
import hla.rti1516e.SynchronizationPointFailureReason;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.FederateInternalError;

public abstract class HLAOnRamp extends OnRamp implements FederateAmbassador {

    public HLAOnRamp(Bridge _bridge, HLAShore _thisShore) {
        super(_bridge, _thisShore);
    }

    protected abstract boolean subscribe(RTIambassador rti);

    @Override
    public void connectionLost(String faultDescription) throws FederateInternalError {
    }

    @Override
    public void reportFederationExecutions(FederationExecutionInformationSet theFederationExecutionInformationSet) throws FederateInternalError {
    }

    @Override
    public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel) throws FederateInternalError {
    }

    @Override
    public void synchronizationPointRegistrationFailed(String synchronizationPointLabel, SynchronizationPointFailureReason reason) throws FederateInternalError {
    }

    @Override
    public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag) throws FederateInternalError {
    }

    @Override
    public void federationSynchronized(String synchronizationPointLabel, FederateHandleSet failedToSyncSet) throws FederateInternalError {
    }

    @Override
    public void initiateFederateSave(String label) throws FederateInternalError {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void initiateFederateSave(String label, LogicalTime time) throws FederateInternalError {
    }

    @Override
    public void federationSaved() throws FederateInternalError {
    }

    @Override
    public void federationNotSaved(SaveFailureReason reason) throws FederateInternalError {
    }

    @Override
    public void federationSaveStatusResponse(FederateHandleSaveStatusPair[] response) throws FederateInternalError {
    }

    @Override
    public void requestFederationRestoreSucceeded(String label) throws FederateInternalError {
    }

    @Override
    public void requestFederationRestoreFailed(String label) throws FederateInternalError {
    }

    @Override
    public void federationRestoreBegun() throws FederateInternalError {
    }

    @Override
    public void initiateFederateRestore(String label, String federateName, FederateHandle federateHandle) throws FederateInternalError {
    }

    @Override
    public void federationRestored() throws FederateInternalError {
    }

    @Override
    public void federationNotRestored(RestoreFailureReason reason) throws FederateInternalError {
    }

    @Override
    public void federationRestoreStatusResponse(FederateRestoreStatus[] response) throws FederateInternalError {
    }

    @Override
    public void startRegistrationForObjectClass(ObjectClassHandle theClass) throws FederateInternalError {
    }

    @Override
    public void stopRegistrationForObjectClass(ObjectClassHandle theClass) throws FederateInternalError {
    }

    @Override
    public void turnInteractionsOn(InteractionClassHandle theHandle) throws FederateInternalError {
    }

    @Override
    public void turnInteractionsOff(InteractionClassHandle theHandle) throws FederateInternalError {
    }

    @Override
    public void objectInstanceNameReservationSucceeded(String objectName) throws FederateInternalError {
    }

    @Override
    public void objectInstanceNameReservationFailed(String objectName) throws FederateInternalError {
    }

    @Override
    public void multipleObjectInstanceNameReservationSucceeded(Set<String> objectNames) throws FederateInternalError {
    }

    @Override
    public void multipleObjectInstanceNameReservationFailed(Set<String> objectNames) throws FederateInternalError {
    }

    @Override
    public void discoverObjectInstance(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass, String objectName) throws FederateInternalError {
    }

    @Override
    public void discoverObjectInstance(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass, String objectName, FederateHandle producingFederate) throws FederateInternalError {
    }

    @Override
    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
    }

    @Override
    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
    }

    @Override
    public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering, SupplementalRemoveInfo removeInfo) throws FederateInternalError {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering, LogicalTime theTime, OrderType receivedOrdering, SupplementalRemoveInfo removeInfo) throws FederateInternalError {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalRemoveInfo removeInfo) throws FederateInternalError {
    }

    @Override
    public void attributesInScope(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
    }

    @Override
    public void attributesOutOfScope(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
    }

    @Override
    public void provideAttributeValueUpdate(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, byte[] userSuppliedTag) throws FederateInternalError {
    }

    @Override
    public void turnUpdatesOnForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
    }

    @Override
    public void turnUpdatesOnForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, String updateRateDesignator) throws FederateInternalError {
    }

    @Override
    public void turnUpdatesOffForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
    }

    @Override
    public void confirmAttributeTransportationTypeChange(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, TransportationTypeHandle theTransportation) throws FederateInternalError {
    }

    @Override
    public void reportAttributeTransportationType(ObjectInstanceHandle theObject, AttributeHandle theAttribute, TransportationTypeHandle theTransportation) throws FederateInternalError {
    }

    @Override
    public void confirmInteractionTransportationTypeChange(InteractionClassHandle theInteraction, TransportationTypeHandle theTransportation) throws FederateInternalError {
    }

    @Override
    public void reportInteractionTransportationType(FederateHandle theFederate, InteractionClassHandle theInteraction, TransportationTypeHandle theTransportation) throws FederateInternalError {
    }

    @Override
    public void requestAttributeOwnershipAssumption(ObjectInstanceHandle theObject, AttributeHandleSet offeredAttributes, byte[] userSuppliedTag) throws FederateInternalError {
    }

    @Override
    public void requestDivestitureConfirmation(ObjectInstanceHandle theObject, AttributeHandleSet offeredAttributes) throws FederateInternalError {
    }

    @Override
    public void attributeOwnershipAcquisitionNotification(ObjectInstanceHandle theObject, AttributeHandleSet securedAttributes, byte[] userSuppliedTag) throws FederateInternalError {
    }

    @Override
    public void attributeOwnershipUnavailable(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
    }

    @Override
    public void requestAttributeOwnershipRelease(ObjectInstanceHandle theObject, AttributeHandleSet candidateAttributes, byte[] userSuppliedTag) throws FederateInternalError {
    }

    @Override
    public void confirmAttributeOwnershipAcquisitionCancellation(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
    }

    @Override
    public void informAttributeOwnership(ObjectInstanceHandle theObject, AttributeHandle theAttribute, FederateHandle theOwner) throws FederateInternalError {
    }

    @Override
    public void attributeIsNotOwned(ObjectInstanceHandle theObject, AttributeHandle theAttribute) throws FederateInternalError {
    }

    @Override
    public void attributeIsOwnedByRTI(ObjectInstanceHandle theObject, AttributeHandle theAttribute) throws FederateInternalError {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void timeRegulationEnabled(LogicalTime time) throws FederateInternalError {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void timeConstrainedEnabled(LogicalTime time) throws FederateInternalError {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void timeAdvanceGrant(LogicalTime theTime) throws FederateInternalError {
    }

    @Override
    public void requestRetraction(MessageRetractionHandle theHandle) throws FederateInternalError {
    }
}
