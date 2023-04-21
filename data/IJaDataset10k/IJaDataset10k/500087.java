package org.ourgrid.common.job;

import org.ourgrid.common.interfaces.to.GridProcessHandle;

public class IllegalResultException extends RuntimeException {

    private static final long serialVersionUID = 40L;

    public IllegalResultException(String msg, int jobID, int taskID) {
        super("Illegal result received for Task " + jobID + "." + taskID + ": " + msg);
    }

    public IllegalResultException(String msg, GridProcessHandle replicaHandle) {
        super("Illegal result received for Replica " + replicaHandle + ": " + msg);
    }
}
