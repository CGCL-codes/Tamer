package visad;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
   RemoteThingReferenceImpl is VisAD remote adapter for ThingReferenceImpl.<P>
*/
public class RemoteThingReferenceImpl extends UnicastRemoteObject implements RemoteThingReference {

    final transient ThingReferenceImpl AdaptedThingReference;

    public RemoteThingReferenceImpl(ThingReferenceImpl ref) throws RemoteException {
        AdaptedThingReference = ref;
    }

    /** set this RemoteThingReferenceImpl to refer to t;
      must be RemoteThingImpl */
    public synchronized void setThing(Thing t) throws VisADException, RemoteException {
        if (t == null) {
            throw new ReferenceException("RemoteThingReferenceImpl: thing " + "cannot be null");
        }
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteThingReferenceImpl.setThing: " + "AdaptedThingReference is null");
        }
        if (t instanceof ThingImpl) {
            AdaptedThingReference.setThing(t);
        } else {
            AdaptedThingReference.adaptedSetThing((RemoteThing) t, (RemoteThingReference) this);
        }
    }

    public Thing getThing() throws VisADException, RemoteException {
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteThingReferenceImpl.getThing: " + "AdaptedThingReference is null");
        }
        return AdaptedThingReference.getThing();
    }

    public long getTick() throws VisADException, RemoteException {
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteThingReferenceImpl.getTick: " + "AdaptedThingReference is null");
        }
        return AdaptedThingReference.getTick();
    }

    public long incTick() throws VisADException, RemoteException {
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteThingReferenceImpl.incTick: " + "AdaptedThingReference is null");
        }
        return AdaptedThingReference.incTick();
    }

    public String getName() throws VisADException, RemoteException {
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteThingReferenceImpl.getName: " + "AdaptedThingReference is null");
        }
        return AdaptedThingReference.getName();
    }

    /** addThingChangedListener and removeThingChangedListener
      provide ThingChangedEvent source semantics;
      Action must be RemoteAction */
    public void addThingChangedListener(ThingChangedListener a, long id) throws VisADException, RemoteException {
        if (!(a instanceof RemoteAction)) {
            throw new RemoteVisADException("RemoteThingReferenceImpl.addThingChanged" + "Listener: Action must be Remote");
        }
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteThingReferenceImpl." + "addThingChangedListener: " + "AdaptedThingReference is null");
        }
        AdaptedThingReference.adaptedAddThingChangedListener(((RemoteAction) a), id);
    }

    /** ThingChangedListener must be RemoteAction */
    public void removeThingChangedListener(ThingChangedListener a) throws VisADException, RemoteException {
        if (!(a instanceof RemoteAction)) {
            throw new RemoteVisADException("RemoteThingReferenceImpl.removeThingChanged" + "Listener: Action must be Remote");
        }
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteThingReferenceImpl." + "removeThingChangedListener: " + "AdaptedThingReference is null");
        }
        AdaptedThingReference.adaptedRemoveThingChangedListener(((RemoteAction) a));
    }

    /** Action must be RemoteAction */
    public ThingChangedEvent peekThingChanged(Action a) throws VisADException, RemoteException {
        if (!(a instanceof RemoteAction)) {
            throw new RemoteVisADException("RemoteThingReferenceImpl.acknowledge" + "ThingChanged: Action must be Remote");
        }
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteThingReferenceImpl." + "acknowledgeThingChanged: " + "AdaptedThingReference is null");
        }
        return AdaptedThingReference.adaptedPeekThingChanged(((RemoteAction) a));
    }

    /** Action must be RemoteAction */
    public ThingChangedEvent acknowledgeThingChanged(Action a) throws VisADException, RemoteException {
        if (!(a instanceof RemoteAction)) {
            throw new RemoteVisADException("RemoteThingReferenceImpl.acknowledge" + "ThingChanged: Action must be Remote");
        }
        if (AdaptedThingReference == null) {
            throw new RemoteVisADException("RemoteThingReferenceImpl." + "acknowledgeThingChanged: " + "AdaptedThingReference is null");
        }
        return AdaptedThingReference.adaptedAcknowledgeThingChanged(((RemoteAction) a));
    }
}
