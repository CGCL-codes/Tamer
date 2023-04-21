package org.middleheaven.core.bootstrap;

import org.middleheaven.io.repository.ManagedFile;

/**
 * Represent special meaning locations for the container.
 */
public interface FileContext {

    /**
     * 
     * @return ManagedFile representing a folder where to read environment configuration 
     */
    public ManagedFile getEnvironmentConfigRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read environment data 
     */
    public ManagedFile getEnvironmentDataRepository();

    /**
     * 
     * @return ManagedFile representing the aaplication's reference folder 
     */
    public ManagedFile getAppRootRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read application configuration
     */
    public ManagedFile getAppConfigRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read/write application data
     */
    public ManagedFile getAppDataRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read/write application log
     */
    public ManagedFile getAppLogRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read application classapth
     */
    public ManagedFile getAppClasspathRepository();
}
