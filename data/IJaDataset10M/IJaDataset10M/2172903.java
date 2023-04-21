package org.drdc.Test1516e.restaurant.viewer;

import java.util.Set;
import org.drdc.Test1516e.util.Barrier;
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
import hla.rti1516e.LogicalTimeInterval;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RestoreFailureReason;
import hla.rti1516e.SaveFailureReason;
import hla.rti1516e.SynchronizationPointFailureReason;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.FederateInternalError;

public final class FedAmbImpl<LTI extends LogicalTimeInterval<LTI>, LT extends LogicalTime<LT, LTI>> implements FederateAmbassador {

    private ViewerFrame<LTI, LT> _userInterface;

    private Viewer<LTI, LT> _fed;

    private Barrier<LTI, LT> _enableTimeConstrainedBarrier = null;

    private Barrier<LTI, LT> _enableTimeRegulationBarrier = null;

    private Barrier<LTI, LT> _synchronizationPointRegistrationSucceededBarrier = null;

    private Barrier<LTI, LT> _federationSynchronizedBarrier = null;

    public void setEnableTimeConstrainedBarrier(Barrier<LTI, LT> b) {
        _enableTimeConstrainedBarrier = b;
    }

    public void setEnableTimeRegulationBarrier(Barrier<LTI, LT> b) {
        _enableTimeRegulationBarrier = b;
    }

    public void setFederationSynchronizedBarrier(Barrier<LTI, LT> b) {
        _federationSynchronizedBarrier = b;
    }

    public void setSynchronizationPointRegistrationSucceededBarrier(Barrier<LTI, LT> b) {
        _synchronizationPointRegistrationSucceededBarrier = b;
    }

    public FedAmbImpl(Viewer<LTI, LT> fed, ViewerFrame<LTI, LT> ui) {
        _fed = fed;
        _userInterface = ui;
    }

    @Override
    public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag) throws FederateInternalError {
        _fed.recordSynchronizationPointAnnouncement(synchronizationPointLabel);
    }

    @Override
    public void attributeIsNotOwned(ObjectInstanceHandle theObject, AttributeHandle theAttribute) throws FederateInternalError {
        _userInterface.post("�(7.16)Attribute is not owned, object: " + theObject + ", attribute: " + theAttribute);
    }

    @Override
    public void attributeIsOwnedByRTI(ObjectInstanceHandle theObject, AttributeHandle theAttribute) throws FederateInternalError {
        _userInterface.post("�(7.16)Attribute owned by RTI, object: " + theObject + ", attribute: " + theAttribute);
    }

    @Override
    public void attributeOwnershipAcquisitionNotification(ObjectInstanceHandle theObject, AttributeHandleSet securedAttributes, byte[] userSuppliedTag) throws FederateInternalError {
        _userInterface.post("�(7.6)attributeOwnershipAcquisitionNotification; object:" + theObject + ", attrs: " + securedAttributes);
    }

    @Override
    public void attributeOwnershipUnavailable(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
        _userInterface.post("�(7.9)attributeOwnershipUnavailable; object:" + theObject + ", attrs: " + theAttributes);
    }

    @Override
    public void attributesInScope(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
        _userInterface.post("�(6.15)attributesInScope; object:" + theObject + ", attrs: " + theAttributes);
    }

    @Override
    public void attributesOutOfScope(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
        _userInterface.post("�(6.16)attributesOutOfScope; object:" + theObject + ", attrs: " + theAttributes);
    }

    @Override
    public void confirmAttributeOwnershipAcquisitionCancellation(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
        _userInterface.post("�(7.14)confirmAttributeOwnershipAcquisitionCancellation; object:" + theObject + ", attrs: " + theAttributes);
    }

    @Override
    public void discoverObjectInstance(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass, String objectName) throws FederateInternalError {
        _fed.queueDiscoverObjectInstance(theObject, theObjectClass, objectName);
    }

    @Override
    public void federationNotRestored(RestoreFailureReason reason) throws FederateInternalError {
        _userInterface.post("�(4.21)Federation not restored.");
    }

    @Override
    public void federationNotSaved(SaveFailureReason reason) throws FederateInternalError {
        _userInterface.post("�(4.15)Federation not saved.");
    }

    @Override
    public void federationRestoreBegun() throws FederateInternalError {
        _userInterface.post("�(4.18)Federation restore begun.");
    }

    @Override
    public void federationRestored() throws FederateInternalError {
        _userInterface.post("�(4.21)Federation restored.");
    }

    @Override
    public void federationSaved() throws FederateInternalError {
        _userInterface.post("�(4.15)Federation saved.");
    }

    @Override
    public void federationSynchronized(String synchronizationPointLabel, FederateHandleSet failedToSyncSet) throws FederateInternalError {
        if (_federationSynchronizedBarrier != null) {
            if (_federationSynchronizedBarrier.getSuppliedValue().equals(synchronizationPointLabel)) {
                _federationSynchronizedBarrier.lower(null);
            } else {
                _userInterface.post("�(4.10)federationSynchronized at:" + synchronizationPointLabel);
            }
        } else {
            _userInterface.post("ERROR: federationSynchronized with no barrier set");
        }
    }

    @Override
    public void informAttributeOwnership(ObjectInstanceHandle theObject, AttributeHandle theAttribute, FederateHandle theOwner) throws FederateInternalError {
        _userInterface.post("�(7.16)informAttributeOwnership; object:" + theObject + ", attr: " + theAttribute + ", owner: " + theOwner);
    }

    @Override
    public void initiateFederateRestore(String label, String federateName, FederateHandle federateHandle) throws FederateInternalError {
        _userInterface.post("�(4.19)Initiate federate restore.");
    }

    @Override
    public void initiateFederateSave(String label) throws FederateInternalError {
        _userInterface.post("�(4.12)Initiate federate save.");
    }

    @Override
    public void provideAttributeValueUpdate(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, byte[] userSuppliedTag) throws FederateInternalError {
        _userInterface.post("�(6.18)Provide attribute value update, object: " + theObject + ", attributes: " + theAttributes);
    }

    @Override
    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
        _fed.queueReceiveInteractionCallback(interactionClass, theParameters);
    }

    @Override
    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
        _fed.queueReceiveInteractionCallback(interactionClass, theParameters);
    }

    @Override
    public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
        String order = sentOrdering.equals(OrderType.RECEIVE) ? "receive" : "timestamp";
        _userInterface.post("�(6.9)receiveInteraction " + interactionClass + ", order: " + order + ", transportation: " + theTransport + ", tag: " + renderTag(userSuppliedTag));
        _userInterface.post("  time: " + theTime + ", retraction: " + retractionHandle);
        for (ParameterHandle p : theParameters.keySet()) _userInterface.post("  param: " + p + " value: " + theParameters.get(p));
    }

    @Override
    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
        _userInterface.post("�(6.7)reflectAttributeValues of obj " + theObject + ", tag: " + renderTag(userSuppliedTag));
        for (AttributeHandle a : theAttributes.keySet()) _userInterface.post("  attr: " + a + " value: " + theAttributes.get(a));
    }

    @Override
    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
        _fed.queueReflectAttributeValuesEvent((LT) theTime, theObject, theAttributes, userSuppliedTag);
    }

    @Override
    public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes, byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
        _fed.queueReflectAttributeValuesEvent((LT) theTime, theObject, theAttributes, userSuppliedTag);
    }

    @Override
    public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering, SupplementalRemoveInfo removeInfo) throws FederateInternalError {
        _userInterface.post("�(6.11)removeObjectInstance; object:" + theObject + ", tag: " + renderTag(userSuppliedTag));
    }

    @Override
    public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering, LogicalTime theTime, OrderType receivedOrdering, SupplementalRemoveInfo removeInfo) throws FederateInternalError {
        _fed.queueRemoveObjectInstanceEvent((LT) theTime, theObject);
    }

    @Override
    public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering, LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalRemoveInfo removeInfo) throws FederateInternalError {
        _fed.queueRemoveObjectInstanceEvent((LT) theTime, theObject);
    }

    @Override
    public void requestAttributeOwnershipAssumption(ObjectInstanceHandle theObject, AttributeHandleSet offeredAttributes, byte[] userSuppliedTag) throws FederateInternalError {
    }

    @Override
    public void requestAttributeOwnershipRelease(ObjectInstanceHandle theObject, AttributeHandleSet candidateAttributes, byte[] userSuppliedTag) throws FederateInternalError {
        _userInterface.post("�(7.10)requestAttributeOwnershipRelease; object:" + theObject + ", attrs: " + candidateAttributes + ", tag: " + renderTag(userSuppliedTag));
    }

    @Override
    public void requestFederationRestoreFailed(String label) throws FederateInternalError {
        _userInterface.post("�(4.17)Request federation restore failed, label: " + label);
    }

    @Override
    public void requestFederationRestoreSucceeded(String label) throws FederateInternalError {
        _userInterface.post("�(4.17)Request federation restore succeeded, label: " + label);
    }

    @Override
    public void requestRetraction(MessageRetractionHandle theHandle) throws FederateInternalError {
        _userInterface.post("�(8.22)Request retraction, handle: " + theHandle);
    }

    @Override
    public void startRegistrationForObjectClass(ObjectClassHandle theClass) throws FederateInternalError {
        _userInterface.post("�(5.10)startRegistrationForObjectClass:" + theClass);
    }

    @Override
    public void stopRegistrationForObjectClass(ObjectClassHandle theClass) throws FederateInternalError {
        _userInterface.post("�(5.11)stopRegistrationForObjectClass:" + theClass);
    }

    @Override
    public void synchronizationPointRegistrationFailed(String synchronizationPointLabel, SynchronizationPointFailureReason reason) throws FederateInternalError {
        _userInterface.post("�(4.7)synchronizationPointRegistrationFailed; label: " + synchronizationPointLabel);
    }

    @Override
    public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel) throws FederateInternalError {
        if (_synchronizationPointRegistrationSucceededBarrier != null) if (_synchronizationPointRegistrationSucceededBarrier.getSuppliedValue().equals(synchronizationPointLabel)) _synchronizationPointRegistrationSucceededBarrier.lower(null); else _userInterface.post("�(4.7)synchronizationPointRegistrationSucceeded; label: " + synchronizationPointLabel); else _userInterface.post("ERROR: synchronizationPointRegistrationSucceeded with no barrier set");
    }

    @Override
    public void timeAdvanceGrant(LogicalTime theTime) throws FederateInternalError {
        _fed.queueGrantEvent((LT) theTime);
    }

    @Override
    public void timeConstrainedEnabled(LogicalTime theFederateTime) throws FederateInternalError {
        if (_enableTimeConstrainedBarrier != null) _enableTimeConstrainedBarrier.lower((LT) theFederateTime); else _userInterface.post("ERROR: timeConstrainedEnabled with no barrier set");
    }

    @Override
    public void timeRegulationEnabled(LogicalTime theFederateTime) throws FederateInternalError {
        if (_enableTimeRegulationBarrier != null) _enableTimeRegulationBarrier.lower((LT) theFederateTime); else _userInterface.post("ERROR: timeRegulationEnabled with no barrier set");
    }

    @Override
    public void turnInteractionsOff(InteractionClassHandle theHandle) throws FederateInternalError {
        _userInterface.post("�(5.13)turnInteractionsOff:" + theHandle);
    }

    @Override
    public void turnInteractionsOn(InteractionClassHandle theHandle) throws FederateInternalError {
        _userInterface.post("�(5.12)turnInteractionsOn:" + theHandle);
    }

    @Override
    public void turnUpdatesOffForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
        _userInterface.post("�(6.20)Turn updates off for object instance: " + theObject + ", attributes: " + theAttributes);
    }

    @Override
    public void turnUpdatesOnForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes) throws FederateInternalError {
        _userInterface.post("�(6.19)Turn updates on for object instance: " + theObject + ", attributes: " + theAttributes);
    }

    private String renderTag(byte[] tag) {
        return (tag == null) ? "[null]" : new String(tag);
    }

    @Override
    public void confirmAttributeTransportationTypeChange(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, TransportationTypeHandle theTransportation) throws FederateInternalError {
    }

    @Override
    public void confirmInteractionTransportationTypeChange(InteractionClassHandle theInteraction, TransportationTypeHandle theTransportation) throws FederateInternalError {
    }

    @Override
    public void connectionLost(String faultDescription) throws FederateInternalError {
    }

    @Override
    public void discoverObjectInstance(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass, String objectName, FederateHandle producingFederate) throws FederateInternalError {
    }

    @Override
    public void federationRestoreStatusResponse(FederateRestoreStatus[] response) throws FederateInternalError {
    }

    @Override
    public void federationSaveStatusResponse(FederateHandleSaveStatusPair[] response) throws FederateInternalError {
    }

    @Override
    public void initiateFederateSave(String label, LogicalTime time) throws FederateInternalError {
    }

    @Override
    public void multipleObjectInstanceNameReservationFailed(Set<String> objectNames) throws FederateInternalError {
    }

    @Override
    public void multipleObjectInstanceNameReservationSucceeded(Set<String> objectNames) throws FederateInternalError {
    }

    @Override
    public void objectInstanceNameReservationFailed(String objectName) throws FederateInternalError {
    }

    @Override
    public void objectInstanceNameReservationSucceeded(String objectName) throws FederateInternalError {
    }

    @Override
    public void reportAttributeTransportationType(ObjectInstanceHandle theObject, AttributeHandle theAttribute, TransportationTypeHandle theTransportation) throws FederateInternalError {
    }

    @Override
    public void reportFederationExecutions(FederationExecutionInformationSet theFederationExecutionInformationSet) throws FederateInternalError {
    }

    @Override
    public void reportInteractionTransportationType(FederateHandle theFederate, InteractionClassHandle theInteraction, TransportationTypeHandle theTransportation) throws FederateInternalError {
    }

    @Override
    public void requestDivestitureConfirmation(ObjectInstanceHandle theObject, AttributeHandleSet offeredAttributes) throws FederateInternalError {
    }

    @Override
    public void turnUpdatesOnForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes, String updateRateDesignator) throws FederateInternalError {
    }
}
