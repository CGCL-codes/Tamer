package com.teletalk.jserver.pool;

import java.lang.reflect.Constructor;
import com.teletalk.jserver.SubComponent;
import com.teletalk.jserver.util.ReflectionUtils;

/**
 * An pool implementation that contains thread objects that are ready to peform a special function. The ThreadPool class
 * uses three other classes to perform it's function; the PoolThread, PoolWorker and PoolCleaner classes. 
 * <br><br>
 * The PoolThread class implements the threads objects containd in the pool. It is also in essence a wrapper class for 
 * PoolWorker, an abstrat class which contains the implementation for the actual work each thread will perform. When using 
 * a ThreadPool a subclass to PoolWorker must be created and an implementaition for the <code>work</code> method 
 * provided.<br>
 * The preferred way of creating PoolWorkers for the pool is by specifying a {@link PoolWorkerFactory}.
 * <br>
 * <br>
 * The PoolCleaner class is used in ObjectPool to check the consistency of the pool and clean out bad object. When used in conjunction with a ThreadPool, the 
 * PoolCleaner is given the added responsibility to detect errors that may occur any of the threads of the ThreadPool.
 * <br><br>
 * Note that the debug mode flag for each PoolThread/PoolWorker that is initialized will be set to the value of the debug mode property of 
 * this ThreadPool.
 * 
 * @see PoolThread
 * @see PoolWorker
 * @see PoolCleaner
 * @see PoolWorkerFactory
 *  
 * @author Tobias L�fstrand
 * 
 * @since Alpha
 */
public class ThreadPool extends ObjectPool {

    private int idCounter;

    private Class workerClass;

    private Constructor workerClassConstructor;

    private Object[] workerClassConstructorParams;

    private PoolWorkerFactory poolWorkerFactory;

    /**
	 * Creates a ThreadPool. The min size of the pool will be set to maximum size.
	 * 
	 * @param parent the parent of this pool.
	 * @param name name of the pool.
	 * @param maxSize maximum the size of the pool.
	 * @param workerClass the Class of which PoolWorker objects will be created.
	 * @param workerClassConstructorParams the contructorparameters to be used when creating PoolWorker objects.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * 
	 * @exception IllegalArgumentException if workerClass isn't a subclass of PoolWorker or if size or cleanUpInterval was <= 0.
	 * @exception NoSuchMethodException if there was an error finding a constructor in the specified worker class matching the sprecfied contructor parameters.
	 */
    public ThreadPool(SubComponent parent, String name, int maxSize, Class workerClass, Object[] workerClassConstructorParams, long cleanInterval) throws IllegalArgumentException, NoSuchMethodException {
        this(parent, name, maxSize, maxSize, workerClass, workerClassConstructorParams, cleanInterval);
    }

    /**
	 * Creates a ThreadPool.
	 * 
	 * @param parent the parent of this pool.
	 * @param name name of the pool.
	 * @param minSize minimum the size of the pool.
	 * @param maxSize maximum the size of the pool.
	 * @param workerClass the Class of which PoolWorker objects will be created.
	 * @param workerClassConstructorParams the contructorparameters to be used when creating PoolWorker objects.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * 
	 * @exception IllegalArgumentException if workerClass isn't a subclass of PoolWorker or if size or cleanUpInterval was <= 0.
	 * @exception NoSuchMethodException if there was an error finding a constructor in the specified worker class matching the sprecfied contructor parameters.
	 * 
	 * @since 2.0 Build 757
	 */
    public ThreadPool(SubComponent parent, String name, int minSize, int maxSize, Class workerClass, Object[] workerClassConstructorParams, long cleanInterval) throws IllegalArgumentException, NoSuchMethodException {
        super(parent, name, minSize, maxSize, cleanInterval);
        this.workerClass = workerClass;
        this.workerClassConstructor = null;
        this.workerClassConstructorParams = workerClassConstructorParams;
        this.poolWorkerFactory = null;
        if (!((Class) PoolWorker.class).isAssignableFrom(workerClass)) throw new IllegalArgumentException("Workerclass must be a subclass of class PoolWorker!");
        this.workerClassConstructor = ReflectionUtils.findConstructor(workerClass, workerClassConstructorParams);
        idCounter = 0;
    }

    /**
	 * Creates a ThreadPool with a cleaninterval of 60 seconds. The min size of the pool will be set to maximum size.
	 * 
	 * @param parent the parent of this pool.
	 * @param name name of the pool.
	 * @param maxSize maximum the size of the pool.
	 * @param workerClass the Class of which PoolWorker objects will be created.
	 * @param workerClassConstructorParams the contructorparameters to be used when creating PoolWorker objects.
	 * 
	 * @exception IllegalArgumentException if workerClass isn't a subclass of PoolWorker.
	 * @exception NoSuchMethodException if there was an error finding a constructor in the specified worker class matching the sprecfied contructor parameters.
	 */
    public ThreadPool(SubComponent parent, String name, int maxSize, Class workerClass, Object[] workerClassConstructorParams) throws IllegalArgumentException, NoSuchMethodException {
        this(parent, name, maxSize, maxSize, workerClass, workerClassConstructorParams);
    }

    /**
	 * Creates a ThreadPool with a cleaninterval of 60 seconds. The min size of the pool will be set to maximum size.
	 * 
	 * @param parent the parent of this pool.
	 * @param name name of the pool.
	 * @param minSize minimum the size of the pool.
	 * @param maxSize maximum the size of the pool.
	 * @param workerClass the Class of which PoolWorker objects will be created.
	 * @param workerClassConstructorParams the contructorparameters to be used when creating PoolWorker objects.
	 * 
	 * @exception IllegalArgumentException if workerClass isn't a subclass of PoolWorker.
	 * @exception NoSuchMethodException if there was an error finding a constructor in the specified worker class matching the sprecfied contructor parameters.
	 */
    public ThreadPool(SubComponent parent, String name, int minSize, int maxSize, Class workerClass, Object[] workerClassConstructorParams) throws IllegalArgumentException, NoSuchMethodException {
        this(parent, name, minSize, maxSize, workerClass, workerClassConstructorParams, 60 * 1000);
    }

    /**
	 * Creates a ThreadPool. The defaultconstructor of workerClass will be used when creating PoolWorker objects. 
	 * The min size of the pool will be set to maximum size.
	 * 
	 * @param parent the parent of this pool.
	 * @param name name of the pool.
	 * @param maxSize maximum the size of the pool.
	 * @param workerClass the Class of which PoolWorker objects will be created.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * 
	 * @exception IllegalArgumentException if workerClass isn't a subclass of PoolWorker or if size or cleanUpInterval was <= 0.
	 */
    public ThreadPool(SubComponent parent, String name, int maxSize, Class workerClass, long cleanInterval) throws IllegalArgumentException {
        this(parent, name, maxSize, maxSize, workerClass, cleanInterval);
    }

    /**
	 * Creates a ThreadPool. The defaultconstructor of workerClass will be used when creating PoolWorker objects.
	 * 
	 * @param parent the parent of this pool.
	 * @param name name of the pool.
	 * @param maxSize maximum the size of the pool.
	 * @param workerClass the Class of which PoolWorker objects will be created.
	 * @param cleanInterval time in milliseconds between cleanups, used by the associated PoolCleaner.
	 * 
	 * @exception IllegalArgumentException if workerClass isn't a subclass of PoolWorker or if size or cleanUpInterval was <= 0.
	 */
    public ThreadPool(SubComponent parent, String name, int minSize, int maxSize, Class workerClass, long cleanInterval) throws IllegalArgumentException {
        super(parent, name, minSize, maxSize, cleanInterval);
        this.workerClass = workerClass;
        this.workerClassConstructor = null;
        this.workerClassConstructorParams = null;
        this.poolWorkerFactory = null;
        if (!((Class) PoolWorker.class).isAssignableFrom(workerClass)) throw new IllegalArgumentException("Workerclass must be a subclass of class PoolWorker!");
        idCounter = 0;
    }

    /**
	 * Creates a ThreadPool with a cleaninterval of 60 seconds. The defaultconstructor of workerClass will be used when creating PoolWorker objects. 
	 * The min size of the pool will be set to maximum size.
	 * 
	 * @param parent the parent of this pool.
	 * @param name name of the pool.
	 * @param maxSize maximum the size of the pool.
	 * @param workerClass the Class of which PoolWorker objects will be created.
	 * 
	 * @exception IllegalArgumentException if workerClass isn't a subclass of PoolWorker or if size or cleanUpInterval was <= 0.
	 */
    public ThreadPool(SubComponent parent, String name, int maxSize, Class workerClass) throws IllegalArgumentException {
        this(parent, name, maxSize, maxSize, workerClass);
    }

    /**
	 * Creates a ThreadPool with a cleaninterval of 60 seconds. The defaultconstructor of workerClass will be used when creating PoolWorker objects. 
	 * 
	 * @param parent the parent of this pool.
	 * @param name name of the pool.
	 * @param minSize minimum the size of the pool.
	 * @param maxSize maximum the size of the pool.
	 * @param workerClass the Class of which PoolWorker objects will be created.
	 * 
	 * @exception IllegalArgumentException if workerClass isn't a subclass of PoolWorker or if size or cleanUpInterval was <= 0.
	 */
    public ThreadPool(SubComponent parent, String name, int minSize, int maxSize, Class workerClass) throws IllegalArgumentException {
        super(parent, name, minSize, maxSize, 60 * 1000);
        this.workerClass = workerClass;
        this.workerClassConstructor = null;
        this.workerClassConstructorParams = null;
        this.poolWorkerFactory = null;
        if (!((Class) PoolWorker.class).isAssignableFrom(workerClass)) throw new IllegalArgumentException("Workerclass must be a subclass of class PoolWorker!");
        idCounter = 0;
    }

    /**
	 * Creates a ThreadPool with a cleaninterval of 60 seconds.
	 * 
	 * @param parent the parent of this pool.
	 * @param name name of the pool.
	 * @param minSize minimum the size of the pool.
	 * @param maxSize maximum the size of the pool.
	 * @param poolWorkerFactory factory class for creation of pool worker objects.
	 * 
	 * @since 2.0 Build 757
	 */
    public ThreadPool(final SubComponent parent, final String name, final int minSize, final int maxSize, final PoolWorkerFactory poolWorkerFactory) {
        super(parent, name, minSize, maxSize, 60 * 1000);
        this.workerClass = null;
        this.workerClassConstructor = null;
        this.workerClassConstructorParams = null;
        this.poolWorkerFactory = poolWorkerFactory;
        idCounter = 0;
    }

    /**
	 * Creates a new PoolThread object with the specified id. A new PoolWorker object will also be created when creating the PoolThread. 
	 * 
	 * @return a newly created PoolThread object.
	 * 
	 * @exception Exception if an error occured while creating a new instance of the poolwoker class.
	 */
    public PoolThread createPoolThread(int id) throws Exception {
        PoolWorker worker;
        if (this.poolWorkerFactory != null) {
            worker = this.poolWorkerFactory.createPoolWorker();
        } else if (this.workerClassConstructor != null) {
            worker = (PoolWorker) this.workerClassConstructor.newInstance(workerClassConstructorParams);
        } else if (this.workerClass != null) {
            worker = (PoolWorker) this.workerClass.newInstance();
        } else {
            throw new RuntimeException("Unable to create pool woker! PoolWorkerFactory or PoolWorker class not set!");
        }
        return new PoolThread(this, id, super.poolCleaner, worker);
    }

    /**
	 * Convenience method to check out a PoolThread from the pool (using the <code>getPoolThread()</code> method) and initialize it. 
	 * If an error occurred no PoolThread will be initialized and null will be returned. The PoolThread, and consequently the PoolWorker, will be 
	 * initialized with no data (null).
	 * 
	 * @return the checked out PoolThread object or null if an error occurred.
	 * 
	 * @see #getPoolThread()
	 * @see PoolThread#initialize(Object)
	 */
    public PoolThread initializeThread() {
        final PoolThread pt = (PoolThread) this.getPoolThread();
        if (pt != null) pt.initialize(null);
        return pt;
    }

    /**
	 * Convenience method to check out a PoolThread from the pool (using the <code>getPoolThread()</code> method) and initialize it. 
	 * If an error occurred no PoolThread will be initialized and null will be returned.
	 * 
	 * @param data the object which the checked out PoolThread will be initialized with.
	 * 
	 * @return the checked out PoolThread object or null if an error occurred.
	 * 
	 * @see #getPoolThread()
	 * @see PoolThread#initialize(Object)
	 */
    public PoolThread initializeThread(Object data) {
        final PoolThread pt = (PoolThread) this.getPoolThread();
        if (pt != null) pt.initialize(data);
        return pt;
    }

    /**
	 * Convenience method to check out a PoolThread from the pool (using the <code>getPoolThreadWait()</code> method) and initialize it. 
	 * If an error occurred no PoolThread will be initialized and null will be returned. The PoolThread, and consequently the PoolWorker, will be 
	 * initialized with no data (null).
	 * 
	 * @return the checked out PoolThread object or null if an error occurred.
	 * 
	 * @see #getPoolThreadWait()
	 * @see PoolThread#initialize(Object)
	 */
    public PoolThread initializeThreadWait() {
        final PoolThread pt = (PoolThread) this.getPoolThreadWait();
        if (pt != null) pt.initialize(null);
        return pt;
    }

    /**
	 * Convenience method to check out a PoolThread from the pool (using the <code>getPoolThreadWait()</code> method) and initialize it. 
	 * If an error occurred no PoolThread will be initialized and null will be returned.
	 * 
	 * @param data the object which the checked out PoolThread will be initialized with.
	 * 
	 * @return the checked out PoolThread object or null if an error occurred.
	 * 
	 * @see #getPoolThreadWait()
	 * @see PoolThread#initialize(Object)
	 */
    public PoolThread initializeThreadWait(Object data) {
        final PoolThread pt = (PoolThread) this.getPoolThreadWait();
        if (pt != null) pt.initialize(data);
        return pt;
    }

    /**
	 * Checkes out and returns a PoolThread from the pool, but doesn't initialize it. If the pool is empty or there are no valid 
	 * items, this method will block the calling thread until a valid item can be produced.
	 * 
	 * @return an uninitalized PoolThread, null if the pool is disabled. Null is also returned if there was an error during check out or if the calling thread was interrupted.
	 */
    public PoolThread getPoolThreadWait() {
        return (PoolThread) this.checkOutWait();
    }

    /**
	 * Checkes out and returns a PoolThread from the pool, but doesn't initialize it. If the pool is empty or 
	 * there are no valid items, this method will block the calling thread until a valid item can be produced or the 
	 * specified <code>waitTime </code> ellapses.
	 * 
	 * @param waitTime maximum wait time in milliseconds.
	 * 
	 * @return an uninitalized PoolThread, null if the pool is disabled. Null is also returned if there was an error 
	 * during check out or if the calling thread was interrupted.
	 */
    public PoolThread getPoolThreadWait(long waitTime) {
        return (PoolThread) this.checkOutWait(waitTime);
    }

    /**
	 * Checkes out and returns a PoolThread from the pool, but doesn't initialize it. If the pool is empty or 
	 * if there are no valid items, null will be returned.
	 * 
	 * @return an uninitalized PoolThread, null if the pool is disabled, empty or if it contained no valid items. Null is also 
	 * returned if there was an error during check out.
	 */
    public PoolThread getPoolThreadIfAny() {
        return (PoolThread) this.checkOutIfAny();
    }

    /**
	 * Checkes out and returns a PoolThread from the pool, but doesn't initialize it. If the pool is empty or 
	 * if there are no valid items, a new object will be created and returned (though it will not be added to 
	 * the pool when it is eventually checked in).
	 * 
	 * @return an uninitalized PoolThread, null if the pool is disabled. Null is also returned if there was an error during check out.
	 */
    public PoolThread getPoolThread() {
        return (PoolThread) this.checkOut();
    }

    /**
	 * Returns a PoolThread object to the pool
	 * 
	 * @param thread the PoolThread object to be returned to the pool.
	 */
    public void returnToPool(PoolThread thread) {
        this.checkIn(thread);
    }

    /**
	 * Creates a new PoolThread object.
	 * 
	 * @return the newly created PoolThread object.
	 */
    public Object createObject() throws Exception {
        PoolThread thread;
        synchronized (this) {
            thread = createPoolThread(idCounter++);
        }
        thread.start();
        return thread;
    }

    /**
	 * Validates a PoolThread object.
	 * 
	 * @param obj the PoolThread object to validate.
	 * 
	 * @return true if the PoolThread is alive, otherwise false.
	 */
    public boolean validateObject(Object obj, boolean cleanUpValidation) {
        return (obj != null) ? ((PoolThread) obj).validate() : false;
    }

    /**
	 * Finalizes a PoolThread object.
	 * 
	 * @param obj a PoolThread object to be finalized.
	 */
    public void finalizeObject(Object obj) {
        if (obj != null) ((PoolThread) obj).kill();
        obj = null;
    }

    /**
	 * Enables and fills the pool. The pool is filled to
	 * maxSize, if maxSize is larger than 0, otherwise the pool
	 * is filled to lastFillSize.
	 */
    public void doInitialize() {
        super.doInitialize();
    }

    /**
	 * Disables and drains the pool.
	 */
    public void doShutDown() {
        super.doShutDown();
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
    }

    /**
	 * Gets the worker class.
	 * 
	 * @since 2.0 Build 757
	 */
    public Class getWorkerClass() {
        return workerClass;
    }

    /**
    * 
    * @param workerClass
    * 
    * @since 2.0 Build 757
    */
    public void setWorkerClass(Class workerClass) {
        this.workerClass = workerClass;
    }

    /**
    * Gets the worker class constructor.
    * 
    * @since 2.0 Build 757
    */
    public Constructor getWorkerClassConstructor() {
        return workerClassConstructor;
    }

    /**
    * 
    * @param workerClassConstructor
    * 
    * @since 2.0 Build 757
    */
    public void setWorkerClassConstructor(Constructor workerClassConstructor) {
        this.workerClassConstructor = workerClassConstructor;
    }

    /**
    * Gets the worker class constructor params.
    * 
    * @since 2.0 Build 757
    */
    public Object[] getWorkerClassConstructorParams() {
        return workerClassConstructorParams;
    }

    /**
    * 
    * @param workerClassConstructorParams
    * 
    * @since 2.0 Build 757
    */
    public void setWorkerClassConstructorParams(Object[] workerClassConstructorParams) {
        this.workerClassConstructorParams = workerClassConstructorParams;
    }

    /**
    * Gets the pool factory.
    * 
    * @since 2.0 Build 757
    */
    public PoolWorkerFactory getPoolWorkerFactory() {
        return poolWorkerFactory;
    }

    /**
    * 
    * @param poolWorkerFactory
    * 
    * @since 2.0 Build 757
    */
    public void setPoolWorkerFactory(PoolWorkerFactory poolWorkerFactory) {
        this.poolWorkerFactory = poolWorkerFactory;
    }
}
