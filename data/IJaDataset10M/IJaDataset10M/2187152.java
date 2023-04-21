package org.jmlnitrate.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class represents a Thread that can be used by the {@link Kernel} to
 * execute {@link KernelProcess}es.
 *
 * @author Arthur Copeland
 * @version $Revision: 3 $
 *
 */
public final class KernelWorker extends Thread {

    /**
     * Holds the logger
     */
    private static final Log logger = LogFactory.getLog(KernelWorker.class);

    /**
     * Default Ctor.
     */
    public KernelWorker() {
        super();
    }
}
