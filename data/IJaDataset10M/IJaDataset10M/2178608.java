package org.szegedi.spring.web.jsflow;

import org.springframework.core.NestedRuntimeException;

/**
 * Thrown when {@link org.szegedi.spring.web.jsflow.support.AbstractFlowStateStorage}
 * persistence fails.
 * @author Attila Szegedi
 * @version $Id: FlowStateStorageException.java 10 2006-05-16 09:49:48Z szegedia $
 */
public class FlowStateStorageException extends NestedRuntimeException {

    private static final long serialVersionUID = 1L;

    public FlowStateStorageException(String msg) {
        super(msg);
    }

    public FlowStateStorageException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
