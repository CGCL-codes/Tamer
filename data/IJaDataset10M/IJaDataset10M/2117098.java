package com.commsen.stopwatch;

import java.util.Properties;

/**
 * Interface describes the basic functionality a Stopwatch engine should support. All classes
 * implementing this interface are considered Stopwatch engines. Stopwatch can be configured to use
 * given engine by :
 * <ul>
 * <li>setting <code>-Dcom.commsen.stopwatch.engine=&lt;fully_qualified_class_name&gt;</code> JVM
 * parameter</li>
 * <li>creating "stopwatch.properties" file on classpath and seting
 * <code>engine=&lt;fully_qualified_class_name&gt;</code></li>
 * </ul>
 * 
 * @author Milen Dyankov
 * 
 */
public interface StopwatchEngine {

    /**
	 * Method called by Stopwatch when initilializing Stopwatch engine.
	 */
    public void start();

    /**
	 * Called when Stopwatch is disabled or for any other reason it will not use this engine for
	 * some time. This gives engine a chance to force-close all pending measurments, invalidate
	 * caches, close database connections, etc.
	 */
    public void pause();

    /**
	 * Called when Stopwatch is enabled and before attepting to use paused engine. This gives engine
	 * a chance to re-initialize caches, open database connections, etc.
	 */
    public void resume();

    /**
	 * Called when Stopwatch's engine is changed or for any other reason Stopwatch will no longer
	 * use this engine. This gives engine a chance to free all used resources.
	 */
    public void stop();

    /**
	 * Method called when mensuration is started.
	 * 
	 * @param group the name of the group this measurment should be placed in
	 * @param label how this measurment should be labeled
	 * @return Unique ID representing current measurment.
	 */
    public long begin(String group, String label);

    /**
	 * Method called when mensuration is stopped. It is possible to call this method multiple times
	 * with the same id, however the engine should process only the first call.
	 * 
	 * @param id Unique ID representing the actual measurment that need to be stopped.
	 */
    public void end(long id);

    /**
	 * Method called when mensuration with id <code>id</code> is to be skipped. It is possible to
	 * call this method multiple times with the same id, however the engine should process only the
	 * first call.
	 * 
	 * @param id Unique ID representing the actual measurment that need to be stopped.
	 */
    public void skip(long id);

    /**
	 * Method called to instruct engine to use user defined storage
	 * 
	 * @param storage the storage to be used by this engine
	 */
    public void setStorage(StopwatchStorage storage);

    /**
	 * Method called to instruct engine in what mode persistence manager should work in.
	 * 
	 * @param persistenceMode the mode persistence manager should work in.
	 * 
	 * @see com.commsen.stopwatch.storages.StorageManager#NORMAL_MODE
	 * @see com.commsen.stopwatch.storages.StorageManager#THREAD_MODE
	 * @see com.commsen.stopwatch.storages.StorageManager#DELAYED_MODE
	 * 
	 */
    public void setPersistenceMode(int persistenceMode);

    /**
	 * Method called to obtain the current storage class
	 * 
	 * @return the class name of the current storage
	 */
    public String getStorageClass();

    /**
	 * Allows clients to get reference to the stopwatch's storage. Storage is normaly used by
	 * clients to generate reports.
	 * 
	 * @return reference to the storage
	 * 
	 */
    public StopwatchStorage getStorage();

    /**
	 * Instructs engine to disable/enable debug information. The reason for this exist is to be able
	 * to minimize the performance impact Stopwatch may have on the measured application. Generating
	 * debug info consumes additional CPU units, which may become a problem if Stopwatch is heavily
	 * used.
	 * 
	 * Setting this to false (it is false by default) should cause no debug info being generated
	 * even when log4j's level is set to DEBUG.
	 * 
	 * @param debugEnabled should debug information be generated
	 */
    public void setDebugEnabled(boolean debugEnabled);

    /**
	 * Called by Stopwatch to set engine properties .
	 * 
	 * @param properties the properties
	 */
    public void setProperties(Properties properties);

    /**
	 * Returns engine properties.
	 * 
	 * @return engine properties
	 */
    public Properties getProperties();
}
