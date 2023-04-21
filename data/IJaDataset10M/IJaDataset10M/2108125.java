package com.db4o.drs.inside;

import java.util.*;
import com.db4o.drs.*;
import com.db4o.drs.inside.traversal.*;
import com.db4o.foundation.*;
import com.db4o.reflect.*;

public final class GenericReplicationSession implements ReplicationSession {

    private static final int SIZE = 10000;

    private final ReplicationReflector _reflector;

    private final CollectionHandler _collectionHandler;

    private ReplicationProviderInside _providerA;

    private ReplicationProviderInside _providerB;

    private ReplicationProvider _directionTo;

    private final ReplicationEventListener _listener;

    private final Traverser _traverser;

    private long _lastReplicationVersion;

    private Hashtable4 _processedUuids;

    private boolean _isReplicatingOnlyDeletions;

    public GenericReplicationSession(ReplicationProviderInside _peerA, ReplicationProviderInside _peerB) {
        this(_peerA, _peerB, new DefaultReplicationEventListener());
    }

    public GenericReplicationSession(ReplicationProvider providerA, ReplicationProvider providerB, ReplicationEventListener listener) {
        _reflector = ReplicationReflector.getInstance();
        _collectionHandler = new CollectionHandlerImpl(_reflector.reflector());
        _traverser = new GenericTraverser(_reflector.reflector(), _collectionHandler);
        _providerA = (ReplicationProviderInside) providerA;
        _providerB = (ReplicationProviderInside) providerB;
        _listener = listener;
        synchronized (_providerA.getMonitor()) {
            synchronized (_providerB.getMonitor()) {
                _providerA.startReplicationTransaction(_providerB.getSignature());
                _providerB.startReplicationTransaction(_providerA.getSignature());
                if (_providerA.getLastReplicationVersion() != _providerB.getLastReplicationVersion()) throw new RuntimeException("Version numbers must be the same");
                _lastReplicationVersion = _providerA.getLastReplicationVersion();
            }
        }
        resetProcessedUuids();
    }

    public final void checkConflict(Object root) {
        try {
            prepareGraphToBeReplicated(root);
        } finally {
            _providerA.clearAllReferences();
            _providerB.clearAllReferences();
        }
    }

    public final void close() {
        _providerA.destroy();
        _providerB.destroy();
        _providerA = null;
        _providerB = null;
        _processedUuids = null;
    }

    public final void commit() {
        synchronized (_providerA.getMonitor()) {
            synchronized (_providerB.getMonitor()) {
                long maxVersion = _providerA.getCurrentVersion() > _providerB.getCurrentVersion() ? _providerA.getCurrentVersion() : _providerB.getCurrentVersion();
                _providerA.syncVersionWithPeer(maxVersion);
                _providerB.syncVersionWithPeer(maxVersion);
                maxVersion++;
                _providerA.commitReplicationTransaction(maxVersion);
                _providerB.commitReplicationTransaction(maxVersion);
            }
        }
    }

    public final ReplicationProvider providerA() {
        return _providerA;
    }

    public final ReplicationProvider providerB() {
        return _providerB;
    }

    public final void replicate(Object root) {
        try {
            prepareGraphToBeReplicated(root);
            copyStateAcross(_providerA);
            copyStateAcross(_providerB);
            storeChangedObjectsIn(_providerA);
            storeChangedObjectsIn(_providerB);
        } finally {
            _providerA.clearAllReferences();
            _providerB.clearAllReferences();
        }
    }

    public void replicateDeletions(Class extent) {
        replicateDeletions(extent, _providerA);
        replicateDeletions(extent, _providerB);
    }

    private void replicateDeletions(Class extent, ReplicationProviderInside provider) {
        _isReplicatingOnlyDeletions = true;
        try {
            Iterator instances = provider.getStoredObjects(extent).iterator();
            while (instances.hasNext()) replicate(instances.next());
        } finally {
            _isReplicatingOnlyDeletions = false;
        }
    }

    public final void rollback() {
        _providerA.rollbackReplication();
        _providerB.rollbackReplication();
    }

    public final void setDirection(ReplicationProvider replicateFrom, ReplicationProvider replicateTo) {
        if (replicateFrom == _providerA && replicateTo == _providerB) _directionTo = _providerB;
        if (replicateFrom == _providerB && replicateTo == _providerA) _directionTo = _providerA;
    }

    private void prepareGraphToBeReplicated(Object root) {
        _traverser.traverseGraph(root, new InstanceReplicationPreparer(_providerA, _providerB, _directionTo, _listener, _isReplicatingOnlyDeletions, _lastReplicationVersion, _processedUuids, _traverser, _reflector, _collectionHandler));
    }

    private Object arrayClone(Object original, ReflectClass claxx, ReplicationProviderInside sourceProvider) {
        ReflectClass componentType = _reflector.getComponentType(claxx);
        int[] dimensions = _reflector.arrayDimensions(original);
        Object result = _reflector.newArrayInstance(componentType, dimensions);
        Object[] flatContents = _reflector.arrayContents(original);
        if (!(claxx.isSecondClass() || componentType.isSecondClass())) replaceWithCounterparts(flatContents, sourceProvider);
        _reflector.arrayShape(flatContents, 0, result, dimensions, 0);
        return result;
    }

    private void copyFieldValuesAcross(Object src, Object dest, ReflectClass claxx, ReplicationProviderInside sourceProvider) {
        ReflectField[] fields;
        fields = claxx.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            ReflectField field = fields[i];
            if (field.isStatic()) continue;
            if (field.isTransient()) continue;
            field.setAccessible();
            Object value = field.get(src);
            field.set(dest, findCounterpart(value, sourceProvider));
        }
        ReflectClass superclass = claxx.getSuperclass();
        if (superclass == null) return;
        copyFieldValuesAcross(src, dest, superclass, sourceProvider);
    }

    private void copyStateAcross(final ReplicationProviderInside sourceProvider) {
        if (_directionTo == sourceProvider) return;
        sourceProvider.visitCachedReferences(new Visitor4() {

            public void visit(Object obj) {
                copyStateAcross((ReplicationReference) obj, sourceProvider);
            }
        });
    }

    private void copyStateAcross(ReplicationReference sourceRef, ReplicationProviderInside sourceProvider) {
        if (!sourceRef.isMarkedForReplicating()) return;
        copyStateAcross(sourceRef.object(), sourceRef.counterpart(), sourceProvider);
    }

    private void copyStateAcross(Object source, Object dest, final ReplicationProviderInside sourceProvider) {
        ReflectClass claxx = _reflector.forObject(source);
        copyFieldValuesAcross(source, dest, claxx, sourceProvider);
    }

    private void deleteInDestination(ReplicationReference reference, ReplicationProviderInside destination) {
        if (!reference.isMarkedForDeleting()) return;
        destination.replicateDeletion(reference.uuid());
    }

    private Object findCounterpart(Object value, ReplicationProviderInside sourceProvider) {
        if (value == null) return null;
        if (ReplicationPlatform.isValueType(value)) return value;
        ReflectClass claxx = _reflector.forObject(value);
        if (claxx.isArray()) return arrayClone(value, claxx, sourceProvider);
        if (claxx.isSecondClass()) return value;
        if (_collectionHandler.canHandle(value)) {
            return collectionClone(value, claxx, sourceProvider);
        }
        ReplicationReference ref = sourceProvider.produceReference(value, null, null);
        if (ref == null) throw new NullPointerException("unable to find the ref of " + value + " of class " + value.getClass());
        Object result = ref.counterpart();
        if (result == null) throw new NullPointerException("unable to find the counterpart of " + value + " of class " + value.getClass());
        return result;
    }

    private Object collectionClone(Object original, ReflectClass claxx, final ReplicationProviderInside sourceProvider) {
        return _collectionHandler.cloneWithCounterparts(sourceProvider, original, claxx, new CounterpartFinder() {

            public Object findCounterpart(Object original) {
                return GenericReplicationSession.this.findCounterpart(original, sourceProvider);
            }
        });
    }

    private ReplicationProviderInside other(ReplicationProviderInside peer) {
        return peer == _providerA ? _providerB : _providerA;
    }

    private void replaceWithCounterparts(Object[] objects, ReplicationProviderInside sourceProvider) {
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object == null) continue;
            ReplicationReference replicationReference = sourceProvider.produceReference(object, null, null);
            if (replicationReference == null) throw new RuntimeException(sourceProvider + " cannot find ref for " + object);
            objects[i] = replicationReference.counterpart();
        }
    }

    private void resetProcessedUuids() {
        _processedUuids = new Hashtable4(SIZE);
    }

    private void storeChangedCounterpartInDestination(ReplicationReference reference, ReplicationProviderInside destination) {
        boolean markedForReplicating = reference.isMarkedForReplicating();
        if (!markedForReplicating) return;
        destination.storeReplica(reference.counterpart());
    }

    private void storeChangedObjectsIn(final ReplicationProviderInside destination) {
        final ReplicationProviderInside source = other(destination);
        if (_directionTo == source) return;
        destination.visitCachedReferences(new Visitor4() {

            public void visit(Object obj) {
                deleteInDestination((ReplicationReference) obj, destination);
            }
        });
        source.visitCachedReferences(new Visitor4() {

            public void visit(Object obj) {
                storeChangedCounterpartInDestination((ReplicationReference) obj, destination);
            }
        });
    }
}
