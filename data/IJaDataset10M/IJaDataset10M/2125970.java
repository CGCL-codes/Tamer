package com.continuent.tungsten.manager.helper;

public class ClusterPolicyManagerException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ClusterPolicyManagerException() {
    }

    public ClusterPolicyManagerException(String message) {
        super(message);
    }

    public ClusterPolicyManagerException(Throwable cause) {
        super(cause);
    }

    public ClusterPolicyManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
