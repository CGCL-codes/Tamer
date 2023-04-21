package com.migazzi.dm4j.repository;

public class RepositoryAccessException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6611013148284042551L;

    public RepositoryAccessException() {
        super();
    }

    public RepositoryAccessException(String message) {
        super(message);
    }

    public RepositoryAccessException(Throwable cause) {
        super(cause);
    }

    public RepositoryAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
