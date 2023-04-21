package org.qctools4j.exception;

import org.qctools4j.model.permission.PermissionEnum;

/**
 * Permission exception.
 * 
 * @author tszadel
 */
public class PermissionException extends QcException {

    /**
     * Constructor.
     * 
     * @param pPermission The permission.
     */
    public PermissionException(final PermissionEnum pPermission) {
        super("You do not have permission to execute the given action: " + pPermission);
    }
}
