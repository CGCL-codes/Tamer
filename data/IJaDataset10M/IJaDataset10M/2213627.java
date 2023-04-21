package com.ivis.xprocess.framework;

public interface DatasourceException {

    String getMessage();

    String getUuid();

    Throwable getException();

    Severity getSeverity();

    public enum Severity {

        HIGH, MEDIUM, LOW
    }
}
