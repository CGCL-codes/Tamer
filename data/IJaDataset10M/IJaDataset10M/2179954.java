package org.apache.axis2.jaxws.utility;

import org.apache.axis2.jaxws.i18n.Messages;
import org.apache.axis2.jaxws.ExceptionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.concurrent.Executor;

/**
 * A simple Executor implementation that does not create a new thread 
 * for processing work, but just borrows the current thread.
 */
public class SingleThreadedExecutor implements Executor {

    public static final Log log = LogFactory.getLog(SingleThreadedExecutor.class);

    public void execute(Runnable command) {
        if (log.isDebugEnabled()) {
            log.debug("JAX-WS work on SingleThreadedExector started.");
        }
        if (command == null) {
            throw ExceptionFactory.makeWebServiceException(Messages.getMessage("singleThreadedExecutorErr1"));
        }
        command.run();
        if (log.isDebugEnabled()) {
            log.debug("JAX-WS work on SingleThreadedExectuor complete.");
        }
    }
}
