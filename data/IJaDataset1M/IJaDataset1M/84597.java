package org.piuframework.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO
 *
 * @author Dirk Mascher
 */
public class NamedObjectUnknownException extends ServiceException {

    private static final long serialVersionUID = -5890919384630195051L;

    private static final Log log = LogFactory.getLog(NamedObjectUnknownException.class);

    private String objectName;

    public NamedObjectUnknownException(String objectName) {
        super("named object not found");
        this.objectName = objectName;
        log.error(null, this);
    }

    public String getObjectName() {
        return objectName;
    }

    public String getMessage() {
        return new StringBuffer(super.getMessage()).append(", name ").append(objectName).toString();
    }
}
