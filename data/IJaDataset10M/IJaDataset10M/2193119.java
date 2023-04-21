package ca.gc.drdc_rddc.atlantic.dmso;

import hla.rti13.java1.AsynchronousDeliveryAlreadyDisabled;
import hla.rti13.java1.AsynchronousDeliveryAlreadyEnabled;
import hla.rti13.java1.AttributeAcquisitionWasNotRequested;
import hla.rti13.java1.AttributeAlreadyBeingAcquired;
import hla.rti13.java1.AttributeAlreadyBeingDivested;
import hla.rti13.java1.AttributeAlreadyOwned;
import hla.rti13.java1.AttributeDivestitureWasNotRequested;
import hla.rti13.java1.AttributeHandleSet;
import hla.rti13.java1.AttributeNotDefined;
import hla.rti13.java1.AttributeNotOwned;
import hla.rti13.java1.AttributeNotPublished;
import hla.rti13.java1.ConcurrentAccessAttempted;
import hla.rti13.java1.CouldNotOpenFED;
import hla.rti13.java1.DeletePrivilegeNotHeld;
import hla.rti13.java1.DimensionNotDefined;
import hla.rti13.java1.EnableTimeConstrainedPending;
import hla.rti13.java1.EnableTimeRegulationPending;
import hla.rti13.java1.ErrorReadingFED;
import hla.rti13.java1.EventRetractionHandle;
import hla.rti13.java1.FederateAlreadyExecutionMember;
import hla.rti13.java1.FederateAmbassador;
import hla.rti13.java1.FederateHandleSet;
import hla.rti13.java1.FederateLoggingServiceCalls;
import hla.rti13.java1.FederateNotExecutionMember;
import hla.rti13.java1.FederateOwnsAttributes;
import hla.rti13.java1.FederateWasNotAskedToReleaseAttribute;
import hla.rti13.java1.FederatesCurrentlyJoined;
import hla.rti13.java1.FederationExecutionAlreadyExists;
import hla.rti13.java1.FederationExecutionDoesNotExist;
import hla.rti13.java1.FederationTimeAlreadyPassed;
import hla.rti13.java1.InteractionClassNotDefined;
import hla.rti13.java1.InteractionClassNotPublished;
import hla.rti13.java1.InteractionClassNotSubscribed;
import hla.rti13.java1.InteractionParameterNotDefined;
import hla.rti13.java1.InvalidExtents;
import hla.rti13.java1.InvalidFederationTime;
import hla.rti13.java1.InvalidLookahead;
import hla.rti13.java1.InvalidOrderingHandle;
import hla.rti13.java1.InvalidRegionContext;
import hla.rti13.java1.InvalidResignAction;
import hla.rti13.java1.InvalidRetractionHandle;
import hla.rti13.java1.InvalidTransportationHandle;
import hla.rti13.java1.NameNotFound;
import hla.rti13.java1.ObjectAlreadyRegistered;
import hla.rti13.java1.ObjectClassNotDefined;
import hla.rti13.java1.ObjectClassNotPublished;
import hla.rti13.java1.ObjectClassNotSubscribed;
import hla.rti13.java1.ObjectNotKnown;
import hla.rti13.java1.OwnershipAcquisitionPending;
import hla.rti13.java1.RTIambassador;
import hla.rti13.java1.RTIinternalError;
import hla.rti13.java1.Region;
import hla.rti13.java1.RegionInUse;
import hla.rti13.java1.RegionNotKnown;
import hla.rti13.java1.RestoreInProgress;
import hla.rti13.java1.RestoreNotRequested;
import hla.rti13.java1.SaveInProgress;
import hla.rti13.java1.SaveNotInitiated;
import hla.rti13.java1.SpaceNotDefined;
import hla.rti13.java1.SpecifiedSaveLabelDoesNotExist;
import hla.rti13.java1.SuppliedAttributes;
import hla.rti13.java1.SuppliedParameters;
import hla.rti13.java1.SynchronizationPointLabelWasNotAnnounced;
import hla.rti13.java1.TimeAdvanceAlreadyInProgress;
import hla.rti13.java1.TimeConstrainedAlreadyEnabled;
import hla.rti13.java1.TimeConstrainedWasNotEnabled;
import hla.rti13.java1.TimeRegulationAlreadyEnabled;
import hla.rti13.java1.TimeRegulationWasNotEnabled;
import ca.gc.drdc_rddc.atlantic.hla.ReentrantLock;

/**
 * This converts an RTIambassador object into an object which implements the
 * interface RTInterface using the delegate pattern. Oct 12, 2004 1:09:31 PM
 * 
 * @author Dillman
 */
public class DelegateRTI implements RTInterface {

    private ReentrantLock rtiLock = new ReentrantLock();

    public ReentrantLock getLock() {
        return rtiLock;
    }

    /** The real RTI. */
    private RTIambassador rti;

    /**
	 * Constructor.
	 * 
	 * @param theRti
	 */
    public DelegateRTI(RTIambassador theRti) {
        super();
        rti = theRti;
    }

    public void associateRegionForUpdates(Region arg0, int arg1, AttributeHandleSet arg2) throws ObjectNotKnown, AttributeNotDefined, InvalidRegionContext, RegionNotKnown, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.associateRegionForUpdates(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public void attributeOwnershipAcquisition(int arg0, AttributeHandleSet arg1, String arg2) throws ObjectNotKnown, ObjectClassNotPublished, AttributeNotDefined, AttributeNotPublished, FederateOwnsAttributes, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.attributeOwnershipAcquisition(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public void attributeOwnershipAcquisitionIfAvailable(int arg0, AttributeHandleSet arg1) throws ObjectNotKnown, ObjectClassNotPublished, AttributeNotDefined, AttributeNotPublished, FederateOwnsAttributes, AttributeAlreadyBeingAcquired, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.attributeOwnershipAcquisitionIfAvailable(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public AttributeHandleSet attributeOwnershipReleaseResponse(int arg0, AttributeHandleSet arg1) throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, FederateWasNotAskedToReleaseAttribute, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        AttributeHandleSet ahs = null;
        rtiLock.lock();
        try {
            ahs = rti.attributeOwnershipReleaseResponse(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
        return ahs;
    }

    public void cancelAttributeOwnershipAcquisition(int arg0, AttributeHandleSet arg1) throws ObjectNotKnown, AttributeNotDefined, AttributeAlreadyOwned, AttributeAcquisitionWasNotRequested, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.cancelAttributeOwnershipAcquisition(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void cancelNegotiatedAttributeOwnershipDivestiture(int arg0, AttributeHandleSet arg1) throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, AttributeDivestitureWasNotRequested, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.cancelNegotiatedAttributeOwnershipDivestiture(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void changeAttributeOrderType(int arg0, AttributeHandleSet arg1, int arg2) throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, InvalidOrderingHandle, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.changeAttributeOrderType(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public void changeAttributeTransportationType(int arg0, AttributeHandleSet arg1, int arg2) throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, InvalidTransportationHandle, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.changeAttributeTransportationType(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public void changeInteractionOrderType(int arg0, int arg1) throws InteractionClassNotDefined, InteractionClassNotPublished, InvalidOrderingHandle, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.changeInteractionOrderType(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void changeInteractionTransportationType(int arg0, int arg1) throws InteractionClassNotDefined, InteractionClassNotPublished, InvalidTransportationHandle, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.changeInteractionTransportationType(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void createFederationExecution(String arg0, String arg1) throws FederationExecutionAlreadyExists, CouldNotOpenFED, ErrorReadingFED, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            rti.createFederationExecution(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public Region createRegion(int arg0, int arg1) throws SpaceNotDefined, InvalidExtents, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        Region r = null;
        rtiLock.lock();
        try {
            r = rti.createRegion(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
        return r;
    }

    public EventRetractionHandle deleteObjectInstance(int arg0, byte[] arg1, String arg2) throws ObjectNotKnown, DeletePrivilegeNotHeld, InvalidFederationTime, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        EventRetractionHandle erh = null;
        rtiLock.lock();
        try {
            erh = rti.deleteObjectInstance(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
        return erh;
    }

    public void deleteObjectInstance(int arg0, String arg1) throws ObjectNotKnown, DeletePrivilegeNotHeld, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.deleteObjectInstance(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void deleteRegion(Region arg0) throws RegionNotKnown, RegionInUse, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.deleteRegion(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void destroyFederationExecution(String arg0) throws FederatesCurrentlyJoined, FederationExecutionDoesNotExist, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            rti.destroyFederationExecution(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void disableAsynchronousDelivery() throws AsynchronousDeliveryAlreadyDisabled, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.disableAsynchronousDelivery();
        } finally {
            rtiLock.unlock();
        }
    }

    public void disableAttributeRelevanceAdvisorySwitch() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.disableAttributeRelevanceAdvisorySwitch();
        } finally {
            rtiLock.unlock();
        }
    }

    public void disableAttributeScopeAdvisorySwitch() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.disableAttributeScopeAdvisorySwitch();
        } finally {
            rtiLock.unlock();
        }
    }

    public void disableClassRelevanceAdvisorySwitch() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.disableClassRelevanceAdvisorySwitch();
        } finally {
            rtiLock.unlock();
        }
    }

    public void disableInteractionRelevanceAdvisorySwitch() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.disableInteractionRelevanceAdvisorySwitch();
        } finally {
            rtiLock.unlock();
        }
    }

    public void disableTimeConstrained() throws TimeConstrainedWasNotEnabled, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.disableTimeConstrained();
        } finally {
            rtiLock.unlock();
        }
    }

    public void disableTimeRegulation() throws TimeRegulationWasNotEnabled, ConcurrentAccessAttempted, FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.disableTimeRegulation();
        } finally {
            rtiLock.unlock();
        }
    }

    public void enableAsynchronousDelivery() throws AsynchronousDeliveryAlreadyEnabled, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.enableAsynchronousDelivery();
        } finally {
            rtiLock.unlock();
        }
    }

    public void enableAttributeRelevanceAdvisorySwitch() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.enableAttributeRelevanceAdvisorySwitch();
        } finally {
            rtiLock.unlock();
        }
    }

    public void enableAttributeScopeAdvisorySwitch() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.enableAttributeScopeAdvisorySwitch();
        } finally {
            rtiLock.unlock();
        }
    }

    public void enableClassRelevanceAdvisorySwitch() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.enableClassRelevanceAdvisorySwitch();
        } finally {
            rtiLock.unlock();
        }
    }

    public void enableInteractionRelevanceAdvisorySwitch() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.enableInteractionRelevanceAdvisorySwitch();
        } finally {
            rtiLock.unlock();
        }
    }

    public void enableTimeConstrained() throws TimeConstrainedAlreadyEnabled, EnableTimeConstrainedPending, TimeAdvanceAlreadyInProgress, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.enableTimeConstrained();
        } finally {
            rtiLock.unlock();
        }
    }

    public void enableTimeRegulation(byte[] arg0, byte[] arg1) throws TimeRegulationAlreadyEnabled, EnableTimeRegulationPending, TimeAdvanceAlreadyInProgress, InvalidFederationTime, InvalidLookahead, ConcurrentAccessAttempted, FederateNotExecutionMember, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.enableTimeRegulation(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public boolean equals(Object obj) {
        return rti.equals(obj);
    }

    public void federateRestoreComplete() throws RestoreNotRequested, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.federateRestoreComplete();
        } finally {
            rtiLock.unlock();
        }
    }

    public void federateRestoreNotComplete() throws RestoreNotRequested, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.federateRestoreNotComplete();
        } finally {
            rtiLock.unlock();
        }
    }

    public void federateSaveBegun() throws SaveNotInitiated, FederateNotExecutionMember, ConcurrentAccessAttempted, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.federateSaveBegun();
        } finally {
            rtiLock.unlock();
        }
    }

    public void federateSaveComplete() throws SaveNotInitiated, FederateNotExecutionMember, ConcurrentAccessAttempted, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.federateSaveComplete();
        } finally {
            rtiLock.unlock();
        }
    }

    public void federateSaveNotComplete() throws SaveNotInitiated, FederateNotExecutionMember, ConcurrentAccessAttempted, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.federateSaveNotComplete();
        } finally {
            rtiLock.unlock();
        }
    }

    public void flushQueueRequest(byte[] arg0) throws InvalidFederationTime, FederationTimeAlreadyPassed, TimeAdvanceAlreadyInProgress, EnableTimeRegulationPending, EnableTimeConstrainedPending, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.flushQueueRequest(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getAttributeHandle(String arg0, int arg1) throws ObjectClassNotDefined, NameNotFound, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getAttributeHandle(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public String getAttributeName(int arg0, int arg1) throws ObjectClassNotDefined, AttributeNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getAttributeName(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getAttributeRoutingSpaceHandle(int arg0, int arg1) throws ObjectClassNotDefined, AttributeNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getAttributeRoutingSpaceHandle(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getDimensionHandle(String arg0, int arg1) throws SpaceNotDefined, NameNotFound, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getDimensionHandle(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public String getDimensionName(int arg0, int arg1) throws SpaceNotDefined, DimensionNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getDimensionName(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getInteractionClassHandle(String arg0) throws NameNotFound, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getInteractionClassHandle(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public String getInteractionClassName(int arg0) throws InteractionClassNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getInteractionClassName(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getInteractionRoutingSpaceHandle(int arg0) throws InteractionClassNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getInteractionRoutingSpaceHandle(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getObjectClass(int arg0) throws ObjectNotKnown, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getObjectClass(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getObjectClassHandle(String arg0) throws NameNotFound, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getObjectClassHandle(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public String getObjectClassName(int arg0) throws ObjectClassNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getObjectClassName(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getObjectInstanceHandle(String arg0) throws ObjectNotKnown, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getObjectInstanceHandle(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public String getObjectInstanceName(int arg0) throws ObjectNotKnown, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getObjectInstanceName(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getOrderingHandle(String arg0) throws NameNotFound, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getOrderingHandle(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public String getOrderingName(int arg0) throws InvalidOrderingHandle, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getOrderingName(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getParameterHandle(String arg0, int arg1) throws InteractionClassNotDefined, NameNotFound, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getParameterHandle(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public String getParameterName(int arg0, int arg1) throws InteractionClassNotDefined, InteractionParameterNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getParameterName(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public Region getRegion(int arg0) throws FederateNotExecutionMember, ConcurrentAccessAttempted, RegionNotKnown, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getRegion(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getRegionToken(Region arg0) throws FederateNotExecutionMember, ConcurrentAccessAttempted, RegionNotKnown, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getRegionToken(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getRoutingSpaceHandle(String arg0) throws NameNotFound, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getRoutingSpaceHandle(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public String getRoutingSpaceName(int arg0) throws SpaceNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getRoutingSpaceName(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int getTransportationHandle(String arg0) throws NameNotFound, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getTransportationHandle(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public String getTransportationName(int arg0) throws InvalidTransportationHandle, FederateNotExecutionMember, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.getTransportationName(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int hashCode() {
        return rti.hashCode();
    }

    public boolean isAttributeOwnedByFederate(int arg0, int arg1) throws ObjectNotKnown, AttributeNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.isAttributeOwnedByFederate(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public int joinFederationExecution(String arg0, String arg1, FederateAmbassador arg2) throws FederateAlreadyExecutionMember, FederationExecutionDoesNotExist, CouldNotOpenFED, ErrorReadingFED, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.joinFederationExecution(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public void localDeleteObjectInstance(int arg0) throws ObjectNotKnown, FederateOwnsAttributes, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.localDeleteObjectInstance(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void modifyLookahead(byte[] arg0) throws InvalidLookahead, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.modifyLookahead(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void negotiatedAttributeOwnershipDivestiture(int arg0, AttributeHandleSet arg1, String arg2) throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, AttributeAlreadyBeingDivested, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.negotiatedAttributeOwnershipDivestiture(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public void nextEventRequest(byte[] arg0) throws InvalidFederationTime, FederationTimeAlreadyPassed, TimeAdvanceAlreadyInProgress, EnableTimeRegulationPending, EnableTimeConstrainedPending, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.nextEventRequest(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void nextEventRequestAvailable(byte[] arg0) throws InvalidFederationTime, FederationTimeAlreadyPassed, TimeAdvanceAlreadyInProgress, EnableTimeRegulationPending, EnableTimeConstrainedPending, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.nextEventRequestAvailable(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void notifyAboutRegionModification(Region arg0) throws RegionNotKnown, InvalidExtents, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.notifyAboutRegionModification(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void publishInteractionClass(int arg0) throws InteractionClassNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.publishInteractionClass(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void publishObjectClass(int arg0, AttributeHandleSet arg1) throws ObjectClassNotDefined, AttributeNotDefined, OwnershipAcquisitionPending, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.publishObjectClass(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void queryAttributeOwnership(int arg0, int arg1) throws ObjectNotKnown, AttributeNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.queryAttributeOwnership(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public byte[] queryFederateTime() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.queryFederateTime();
        } finally {
            rtiLock.unlock();
        }
    }

    public byte[] queryLBTS() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.queryLBTS();
        } finally {
            rtiLock.unlock();
        }
    }

    public byte[] queryLookahead() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.queryLookahead();
        } finally {
            rtiLock.unlock();
        }
    }

    public byte[] queryMinNextEventTime() throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.queryMinNextEventTime();
        } finally {
            rtiLock.unlock();
        }
    }

    public void registerFederationSynchronizationPoint(String arg0, String arg1, FederateHandleSet arg2) throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.registerFederationSynchronizationPoint(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public void registerFederationSynchronizationPoint(String arg0, String arg1) throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.registerFederationSynchronizationPoint(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public int registerObjectInstance(int arg0, String arg1) throws ObjectClassNotDefined, ObjectClassNotPublished, ObjectAlreadyRegistered, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.registerObjectInstance(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public int registerObjectInstance(int arg0) throws ObjectClassNotDefined, ObjectClassNotPublished, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.registerObjectInstance(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public int registerObjectInstanceWithRegion(int arg0, int[] arg1, Region[] arg2) throws ObjectClassNotDefined, ObjectClassNotPublished, AttributeNotDefined, AttributeNotPublished, RegionNotKnown, InvalidRegionContext, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.registerObjectInstanceWithRegion(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public int registerObjectInstanceWithRegion(int arg0, String arg1, int[] arg2, Region[] arg3) throws ObjectClassNotDefined, ObjectClassNotPublished, AttributeNotDefined, AttributeNotPublished, RegionNotKnown, InvalidRegionContext, ObjectAlreadyRegistered, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.registerObjectInstanceWithRegion(arg0, arg1, arg2, arg3);
        } finally {
            rtiLock.unlock();
        }
    }

    public void requestClassAttributeValueUpdate(int arg0, AttributeHandleSet arg1) throws ObjectClassNotDefined, AttributeNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.requestClassAttributeValueUpdate(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void requestClassAttributeValueUpdateWithRegion(int arg0, AttributeHandleSet arg1, Region arg2) throws ObjectClassNotDefined, AttributeNotDefined, RegionNotKnown, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.requestClassAttributeValueUpdateWithRegion(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public void requestFederationRestore(String arg0) throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.requestFederationRestore(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void requestFederationSave(String arg0, byte[] arg1) throws FederationTimeAlreadyPassed, InvalidFederationTime, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.requestFederationSave(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void requestFederationSave(String arg0) throws FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.requestFederationSave(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void requestObjectAttributeValueUpdate(int arg0, AttributeHandleSet arg1) throws ObjectNotKnown, AttributeNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.requestObjectAttributeValueUpdate(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void resignFederationExecution(int arg0) throws FederateOwnsAttributes, FederateNotExecutionMember, InvalidResignAction, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            rti.resignFederationExecution(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void retract(EventRetractionHandle arg0) throws InvalidRetractionHandle, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.retract(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public EventRetractionHandle sendInteraction(int arg0, SuppliedParameters arg1, byte[] arg2, String arg3) throws InteractionClassNotDefined, InteractionClassNotPublished, InteractionParameterNotDefined, InvalidFederationTime, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.sendInteraction(arg0, arg1, arg2, arg3);
        } finally {
            rtiLock.unlock();
        }
    }

    public void sendInteraction(int arg0, SuppliedParameters arg1, String arg2) throws InteractionClassNotDefined, InteractionClassNotPublished, InteractionParameterNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.sendInteraction(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public EventRetractionHandle sendInteractionWithRegion(int arg0, SuppliedParameters arg1, byte[] arg2, String arg3, Region arg4) throws InteractionClassNotDefined, InteractionClassNotPublished, InteractionParameterNotDefined, InvalidFederationTime, RegionNotKnown, InvalidRegionContext, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.sendInteractionWithRegion(arg0, arg1, arg2, arg3, arg4);
        } finally {
            rtiLock.unlock();
        }
    }

    public void sendInteractionWithRegion(int arg0, SuppliedParameters arg1, String arg2, Region arg3) throws InteractionClassNotDefined, InteractionClassNotPublished, InteractionParameterNotDefined, RegionNotKnown, InvalidRegionContext, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.sendInteractionWithRegion(arg0, arg1, arg2, arg3);
        } finally {
            rtiLock.unlock();
        }
    }

    public void subscribeInteractionClass(int arg0) throws InteractionClassNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, FederateLoggingServiceCalls, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.subscribeInteractionClass(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void subscribeInteractionClassPassively(int arg0) throws InteractionClassNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, FederateLoggingServiceCalls, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.subscribeInteractionClassPassively(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void subscribeInteractionClassPassivelyWithRegion(int arg0, Region arg1) throws InteractionClassNotDefined, RegionNotKnown, InvalidRegionContext, FederateLoggingServiceCalls, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.subscribeInteractionClassPassivelyWithRegion(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void subscribeInteractionClassWithRegion(int arg0, Region arg1) throws InteractionClassNotDefined, RegionNotKnown, InvalidRegionContext, FederateLoggingServiceCalls, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.subscribeInteractionClassWithRegion(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void subscribeObjectClassAttributes(int arg0, AttributeHandleSet arg1) throws ObjectClassNotDefined, AttributeNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.subscribeObjectClassAttributes(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void subscribeObjectClassAttributesPassively(int arg0, AttributeHandleSet arg1) throws ObjectClassNotDefined, AttributeNotDefined, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.subscribeObjectClassAttributesPassively(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void subscribeObjectClassAttributesPassivelyWithRegion(int arg0, Region arg1, AttributeHandleSet arg2) throws ObjectClassNotDefined, AttributeNotDefined, RegionNotKnown, InvalidRegionContext, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.subscribeObjectClassAttributesPassivelyWithRegion(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public void subscribeObjectClassAttributesWithRegion(int arg0, Region arg1, AttributeHandleSet arg2) throws ObjectClassNotDefined, AttributeNotDefined, RegionNotKnown, InvalidRegionContext, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.subscribeObjectClassAttributesWithRegion(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }

    public void synchronizationPointAchieved(String arg0) throws SynchronizationPointLabelWasNotAnnounced, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.synchronizationPointAchieved(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public boolean tick() throws SpecifiedSaveLabelDoesNotExist, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.tick();
        } finally {
            rtiLock.unlock();
        }
    }

    public boolean tick(double arg0, double arg1) throws SpecifiedSaveLabelDoesNotExist, ConcurrentAccessAttempted, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.tick(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void timeAdvanceRequest(byte[] arg0) throws InvalidFederationTime, FederationTimeAlreadyPassed, TimeAdvanceAlreadyInProgress, EnableTimeRegulationPending, EnableTimeConstrainedPending, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.timeAdvanceRequest(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void timeAdvanceRequestAvailable(byte[] arg0) throws InvalidFederationTime, FederationTimeAlreadyPassed, TimeAdvanceAlreadyInProgress, EnableTimeRegulationPending, EnableTimeConstrainedPending, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.timeAdvanceRequestAvailable(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public String toString() {
        return rti.toString();
    }

    public void unassociateRegionForUpdates(Region arg0, int arg1) throws ObjectNotKnown, InvalidRegionContext, RegionNotKnown, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.unassociateRegionForUpdates(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void unconditionalAttributeOwnershipDivestiture(int arg0, AttributeHandleSet arg1) throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.unconditionalAttributeOwnershipDivestiture(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void unpublishInteractionClass(int arg0) throws InteractionClassNotDefined, InteractionClassNotPublished, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.unpublishInteractionClass(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void unpublishObjectClass(int arg0) throws ObjectClassNotDefined, ObjectClassNotPublished, OwnershipAcquisitionPending, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.unpublishObjectClass(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void unsubscribeInteractionClass(int arg0) throws InteractionClassNotDefined, InteractionClassNotSubscribed, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.unsubscribeInteractionClass(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void unsubscribeInteractionClassWithRegion(int arg0, Region arg1) throws InteractionClassNotDefined, InteractionClassNotSubscribed, RegionNotKnown, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.unsubscribeInteractionClassWithRegion(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public void unsubscribeObjectClass(int arg0) throws ObjectClassNotDefined, ObjectClassNotSubscribed, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.unsubscribeObjectClass(arg0);
        } finally {
            rtiLock.unlock();
        }
    }

    public void unsubscribeObjectClassWithRegion(int arg0, Region arg1) throws ObjectClassNotDefined, RegionNotKnown, ObjectClassNotSubscribed, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.unsubscribeObjectClassWithRegion(arg0, arg1);
        } finally {
            rtiLock.unlock();
        }
    }

    public EventRetractionHandle updateAttributeValues(int arg0, SuppliedAttributes arg1, byte[] arg2, String arg3) throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, InvalidFederationTime, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            return rti.updateAttributeValues(arg0, arg1, arg2, arg3);
        } finally {
            rtiLock.unlock();
        }
    }

    public void updateAttributeValues(int arg0, SuppliedAttributes arg1, String arg2) throws ObjectNotKnown, AttributeNotDefined, AttributeNotOwned, FederateNotExecutionMember, ConcurrentAccessAttempted, SaveInProgress, RestoreInProgress, RTIinternalError {
        rtiLock.lock();
        try {
            rti.updateAttributeValues(arg0, arg1, arg2);
        } finally {
            rtiLock.unlock();
        }
    }
}
