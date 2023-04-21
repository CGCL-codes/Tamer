package net.sf.spring.batch;

import java.io.File;

/**
 * Interface to initialize log4j
 * @author willy
 *
 */
public interface LogConfigurator {

    /**
	 * Called by launcher.
	 * @param confDir The configuration directory.
	 * @param batchName The name of the batch. 
	 */
    public abstract void configure(File confDir, String batchName);
}
