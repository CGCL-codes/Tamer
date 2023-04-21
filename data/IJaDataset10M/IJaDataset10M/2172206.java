package prisms.util;

/**
 * A pool of objects that should only be used by one thread at a time
 * 
 * @param <T> The type of resource that is held by this pool
 */
public class ResourcePool<T> {

    /**
	 * A ResourceCreationException may be thrown by a {@link ResourcePool.ResourceCreator} if it is
	 * unable to create a new resource
	 */
    public static class ResourceCreationException extends Exception {

        /**
		 * @param message The message detailing why the resource cannot be created
		 */
        public ResourceCreationException(String message) {
            super(message);
        }

        /**
		 * @param message The message detailing why the resource cannot be created
		 * @param cause The throwable that is the cause of the inability to create the resource
		 */
        public ResourceCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
	 * Creates and destroys resources on demand for the pool
	 * 
	 * @param <T> The type of value to create and destroy
	 */
    public interface ResourceCreator<T> {

        /**
		 * Creates a new resource to be available to the pool
		 * 
		 * @return The new resource
		 * @throws ResourceCreationException If the new resource cannot be created for any reason
		 */
        T createResource() throws ResourceCreationException;

        /**
		 * Takes care of releasing a resource's system resources when it is no longer needed by the
		 * pool. This will only be needed if the pool's maximum size is reduced below the number of
		 * resources being managed by the pool.
		 * 
		 * @param resource The resource that is no longer being used by the pool
		 */
        void destroyResource(T resource);
    }

    private final java.util.concurrent.locks.ReentrantLock theLock;

    private final java.util.ArrayList<T> theAvailableResources;

    private final java.util.ArrayList<T> theInUseResources;

    private final java.util.HashSet<T> theRemovedResources;

    private final java.util.LinkedList<Thread> theWaitingThreads;

    private final ResourceCreator<T> theCreator;

    private int theMaxSize;

    private volatile boolean isClosed;

    /**
	 * Creates an empty pool that must be supplied with data via the {@link #addResource(Object)}
	 * method
	 */
    public ResourcePool() {
        this(null, 0);
    }

    /**
	 * Creates an empty pool that is capable of creating its own resources on demand
	 * 
	 * @param creator The creator capable of creating resources and capable of destroying them
	 * @param maxSize The maximum size for the pool
	 */
    public ResourcePool(ResourceCreator<T> creator, int maxSize) {
        theLock = new java.util.concurrent.locks.ReentrantLock();
        theAvailableResources = new java.util.ArrayList<T>();
        theInUseResources = new java.util.ArrayList<T>();
        theRemovedResources = new java.util.HashSet<T>();
        theWaitingThreads = new java.util.LinkedList<Thread>();
        theCreator = creator;
        theMaxSize = maxSize;
    }

    /**
	 * @return The maximum size of this pool
	 * @see #setMaxSize(int)
	 */
    public int getMaxSize() {
        return theMaxSize;
    }

    /**
	 * Sets the maximum number of resources for this pool. This affects when new resources will be
	 * created internally by the resource pool using the creator passed to the
	 * {@link #ResourcePool(ResourceCreator, int)} constructor. The parameter does not limit the
	 * number of resources that may be given to the pool using the {@link #addResource(Object)}
	 * method.
	 * 
	 * If the maximum size of a pool is reduced, some resources may be destroyed (via
	 * {@link ResourceCreator#destroyResource(Object)}).
	 * 
	 * If this resource pool was created without a creator, this attribute has no effect.
	 * 
	 * @param size The maximum size of the pool
	 */
    public void setMaxSize(int size) {
        theMaxSize = size;
    }

    /** @return Whether this resource pool has been marked as closed */
    public boolean isClosed() {
        return isClosed;
    }

    /**
	 * Clears this resource pool. All available resources will be removed (and destroyed if there is
	 * a creator). In-use resources will be removed and destroyed when they are released.
	 */
    public void close() {
        isClosed = true;
        clear();
    }

    /**
	 * Clears out all existing resources in the pool, whether they are available or in use. New
	 * resources can then be created by or for the pool. There is no need to wait for in-use
	 * resources to be released back to the pool. When resources being used when this call is made
	 * are released back to the pool, they will be destroyed appropriately without being exposed as
	 * available, while resources created and used after this call will remain available.
	 */
    public void clear() {
        theLock.lock();
        try {
            java.util.Iterator<T> iter;
            iter = theAvailableResources.iterator();
            while (iter.hasNext()) {
                T res = iter.next();
                if (theCreator != null) theCreator.destroyResource(res);
                iter.remove();
            }
            for (T res : theInUseResources) theRemovedResources.add(res);
        } finally {
            theLock.unlock();
        }
    }

    /** @return The total number of resources managed by this pool */
    public int getResourceCount() {
        return theAvailableResources.size() + theInUseResources.size();
    }

    /** @return The number of resources in this pool available for use */
    public int getAvailableResourceCount() {
        return theAvailableResources.size();
    }

    /** @return The number of resources in this pool currently being used */
    public int getInUseResourceCount() {
        return theInUseResources.size();
    }

    /**
	 * Gets a resource from the pool. The resource will be marked as in-use and will never be given
	 * to another invocation of this method nor destroyed by the creator until the resource is
	 * released. For this reason, it is advisable to use a try/finally structure to ensure that the
	 * resource is released when its use is finished.
	 * 
	 * In the case that a resource is not currently available (i.e. if all available resources are
	 * in use and either there is no creator or the maximum size has been reached), if the wait
	 * parameter is true, this method will block until a resource is available. If the wait
	 * parameter is false, null will be returned.
	 * 
	 * @param wait Whether to wait for the resource rather than accept null if no resources or
	 *        available at the moment
	 * @return The free resource to use, or null if the wait parameter is false and there are no
	 *         free resources
	 * @throws ResourceCreationException If an error occurs when the creator creates a new resource
	 */
    public T getResource(final boolean wait) throws ResourceCreationException {
        if (isClosed) throw new IllegalStateException("This resource pool is closed");
        T ret = null;
        boolean waiting = false;
        int tries = 0;
        Thread toWake = null;
        do {
            try {
                theLock.lock();
                try {
                    if (theAvailableResources.size() == 0) updateResourceSet();
                    Thread ct = null;
                    if (tries == 0 && !theWaitingThreads.isEmpty()) {
                        toWake = theWaitingThreads.removeFirst();
                        if (wait) {
                            waiting = true;
                            if (ct == null) ct = Thread.currentThread();
                            theWaitingThreads.add(ct);
                        }
                    }
                    if (theWaitingThreads.isEmpty() && theAvailableResources.size() > 0) {
                        if (waiting) {
                            if (ct == null) ct = Thread.currentThread();
                            theWaitingThreads.remove(ct);
                        }
                        waiting = false;
                        ret = theAvailableResources.remove(theAvailableResources.size() - 1);
                        theInUseResources.add(ret);
                    } else if (wait && !waiting) {
                        waiting = true;
                        if (ct == null) ct = Thread.currentThread();
                        if (tries > 0) theWaitingThreads.addFirst(ct); else theWaitingThreads.add(ct);
                    }
                } finally {
                    theLock.unlock();
                    if (toWake != null) toWake.interrupt();
                }
                if (waiting) {
                    tries++;
                    try {
                        Thread.sleep(24L * 60 * 60 * 1000);
                    } catch (InterruptedException e) {
                    }
                }
                toWake = null;
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                } else if (e instanceof RuntimeException) throw (RuntimeException) e; else if (e instanceof ResourceCreationException) throw (ResourceCreationException) e; else throw new IllegalStateException("Should not be able to get here", e);
            }
        } while (waiting);
        return ret;
    }

    /**
	 * Releases a used resource back into the pool for use
	 * 
	 * @param resource The resource to release to the pool
	 */
    public void releaseResource(T resource) {
        theLock.lock();
        try {
            theInUseResources.remove(resource);
            if (theRemovedResources.remove(resource)) {
                if (theCreator != null) theCreator.destroyResource(resource);
            } else {
                if ((isClosed && !theWaitingThreads.isEmpty()) || getResourceCount() >= theMaxSize) {
                    if (theCreator != null) theCreator.destroyResource(resource);
                } else addResource(resource);
            }
        } finally {
            theLock.unlock();
        }
    }

    /**
	 * Adds a resource to this set. After the resource is added, it is treated identically to any
	 * other resource in this pool. In particular, if this resource pool is managed by a creator,
	 * the new resource may be destroyed by the pool if the pool's maximum size is reduced to make
	 * the pool too large.
	 * 
	 * @param resource The resource to make available to the pool
	 */
    public void addResource(T resource) {
        if (isClosed && !theWaitingThreads.isEmpty()) throw new IllegalStateException("This resource pool is closed");
        Thread waiting = null;
        theLock.lock();
        try {
            if ((!theWaitingThreads.isEmpty() || !isClosed) && !theAvailableResources.contains(resource)) {
                theAvailableResources.add(resource);
                if (!theWaitingThreads.isEmpty()) waiting = theWaitingThreads.removeFirst();
            }
        } finally {
            theLock.unlock();
        }
        if (waiting != null) waiting.interrupt();
    }

    /**
	 * Removes a resource from this pool. If the resource no longer exists in the pool, false is
	 * returned. If the resource is currently available, it will be removed from the pool completely
	 * and false will be returned. Otherwise, the resource is marked for removal and will not be
	 * returned to the available resource set (nor will it be destroyed by the creator) when it is
	 * released and true will be returned.
	 * 
	 * @param resource The resource to remove from the pool
	 * @return Whether the resource is still being used
	 */
    public boolean removeResource(T resource) {
        theLock.lock();
        try {
            if (theAvailableResources.remove(resource)) return false;
            if (!theInUseResources.contains(resource)) return false;
            if (!theRemovedResources.contains(resource)) theRemovedResources.add(resource);
            return true;
        } finally {
            theLock.unlock();
        }
    }

    private void updateResourceSet() throws ResourceCreationException {
        if (theCreator == null) return;
        int newRC = getNewResourceCount();
        int total = getResourceCount();
        if (newRC < total) {
            int killCount = total - newRC;
            for (; theAvailableResources.size() > 0 && killCount > 0; killCount--) {
                T res = theAvailableResources.remove(theAvailableResources.size() - 1);
                if (theCreator != null) theCreator.destroyResource(res);
            }
        } else if (newRC > total) {
            int spawnCount = newRC - total;
            for (int t = 0; t < spawnCount; t++) theAvailableResources.add(0, theCreator.createResource());
        }
    }

    private int getNewResourceCount() {
        int used = getInUseResourceCount();
        int total = getResourceCount();
        int ret;
        if (used == total) {
            for (ret = 1; ret * ret <= total; ret++) ;
            ret = ret * ret;
        } else {
            int ceilUsedSqrt;
            for (ceilUsedSqrt = 1; ceilUsedSqrt * ceilUsedSqrt < used; ceilUsedSqrt++) ;
            int floorTotalSqrt;
            for (floorTotalSqrt = 1; floorTotalSqrt * floorTotalSqrt <= total; floorTotalSqrt++) ;
            floorTotalSqrt--;
            if (ceilUsedSqrt < floorTotalSqrt - 1) ret = (ceilUsedSqrt + 1) * (ceilUsedSqrt + 1); else ret = total;
        }
        if (ret > theMaxSize) ret = theMaxSize;
        return ret;
    }

    /**
	 * <p>
	 * A test method for this class. This method creates several threads and a pool with not quite
	 * enough resources to go around. The threads all try to grab resources (with a true wait
	 * parameter), hold them shortly, and then release them, attempting to detect faults with an
	 * independent concurrent set.
	 * </p>
	 * 
	 * <p>
	 * This method prints status reports every 30 seconds to System.out. When an error is detected,
	 * it is printed with System.err. An error in this case is defined only as the pool allowing a
	 * single resource to be in use by more than one caller.
	 * </p>
	 * 
	 * <p>
	 * This method never finishes normally--it will self-test as long as the process is not
	 * terminated.
	 * </p>
	 * 
	 * @param args Command-line arguments, ignored
	 */
    public static void main(String[] args) {
        int threadCount = Runtime.getRuntime().availableProcessors() + 2;
        final ResourcePool<Object> pool = new ResourcePool<Object>(null, threadCount - 1);
        for (int i = 0; i < pool.getMaxSize(); i++) pool.addResource(new Object());
        final java.util.concurrent.ConcurrentHashMap<Object, Object> checkSet;
        checkSet = new java.util.concurrent.ConcurrentHashMap<Object, Object>();
        final long start = System.currentTimeMillis();
        final long[] lastStatus = new long[] { start };
        final long[] testTries = new long[1];
        final int[] faults = new int[1];
        Runnable task = new Runnable() {

            public void run() {
                while (true) {
                    Object res = null;
                    try {
                        res = pool.getResource(true);
                        if (checkSet.get(res) != null) {
                            System.err.println("Resource " + Integer.toHexString(res.hashCode()) + " already in use!");
                            faults[0]++;
                        }
                        checkSet.put(res, res);
                        long now = System.currentTimeMillis();
                        if (now - lastStatus[0] >= 30000) {
                            lastStatus[0] = now;
                            System.out.println(testTries[0] + " tries and " + faults[0] + " faults in " + PrismsUtils.printTimeLength(now - start));
                        }
                        testTries[0]++;
                        Thread.yield();
                    } catch (ResourceCreationException e) {
                        e.printStackTrace();
                        continue;
                    } finally {
                        if (res != null) {
                            checkSet.remove(res);
                            pool.releaseResource(res);
                        }
                    }
                }
            }
        };
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threads.length; i++) threads[i] = new Thread(task);
        for (int i = 0; i < threads.length; i++) threads[i].start();
        System.out.println("Started resource pool test at " + PrismsUtils.print(start) + " with " + threadCount + " threads and " + pool.getMaxSize() + " resources");
        try {
            Thread.sleep(10L * 365 * 24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
        }
    }
}
