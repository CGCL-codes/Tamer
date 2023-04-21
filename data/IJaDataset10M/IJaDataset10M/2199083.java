package org.jul.dsm;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ConcurrentSharedMemory extends SharedMemory {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Representation applyPatch(Patch patch) throws RepresentationException {
        lock.writeLock().lock();
        try {
            return super.applyPatch(patch);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void applyPatch(Patch... patches) throws RepresentationException {
        lock.writeLock().lock();
        try {
            super.applyPatch(patches);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void applyPatch(Collection<Patch> patches) throws RepresentationException {
        lock.writeLock().lock();
        try {
            super.applyPatch(patches);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Patch createPatch(Object rootObject) throws RepresentationException {
        lock.writeLock().lock();
        try {
            return super.createPatch(rootObject);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Patch createPatch(Object rootObject, boolean fromScratch) throws RepresentationException {
        lock.writeLock().lock();
        try {
            return super.createPatch(rootObject, fromScratch);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Patch createPatch(Object rootObject, Object object) throws RepresentationException {
        lock.writeLock().lock();
        try {
            return super.createPatch(rootObject, object);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Patch createPatch(long rootObjectId) throws RepresentationException {
        lock.writeLock().lock();
        try {
            return super.createPatch(rootObjectId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Patch createPatch(long rootObjectId, boolean fromScratch) throws RepresentationException {
        lock.writeLock().lock();
        try {
            return super.createPatch(rootObjectId, fromScratch);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Patch createPatch(Object rootObject, Object object, boolean fromScratch) throws RepresentationException {
        lock.writeLock().lock();
        try {
            return super.createPatch(rootObject, object, fromScratch);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Patch clone(long rootObjectId, boolean forceCompleteClone) {
        lock.readLock().lock();
        try {
            return super.clone(rootObjectId, forceCompleteClone);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Patch clone(Object rootObject, boolean forceCompleteClone) {
        lock.readLock().lock();
        try {
            return super.clone(rootObject, forceCompleteClone);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void removeRoot(long rootObjectId) {
        lock.writeLock().lock();
        try {
            super.removeRoot(rootObjectId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeRoot(Object rootObject) {
        lock.writeLock().lock();
        try {
            super.removeRoot(rootObject);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean isRepresented(long id) {
        lock.readLock().lock();
        try {
            return super.isRepresented(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isRepresented(Object reference) {
        lock.readLock().lock();
        try {
            return super.isRepresented(reference);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Representation getRepresentation(long id) {
        lock.readLock().lock();
        try {
            return super.getRepresentation(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Representation getRepresentation(Object reference) {
        lock.readLock().lock();
        try {
            return super.getRepresentation(reference);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Representation findRepresentation(Object object) throws RepresentationException {
        lock.writeLock().lock();
        try {
            return super.findRepresentation(object);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            return super.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    ConcurrentSharedMemory(IdGenerator idGenerator, ConstructorFactory constructorFactory, boolean ignoreTransientFields, boolean ignoreStaticFields) {
        super(idGenerator, constructorFactory, ignoreTransientFields, ignoreStaticFields);
    }
}
