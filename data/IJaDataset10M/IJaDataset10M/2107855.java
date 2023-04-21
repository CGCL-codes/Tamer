package org.jcvi.assembly.analysis;

public interface AnalysisIssue {

    public enum Severity {

        LOW, MEDIUM, HIGH
    }

    Severity getSeverity();

    String getMessage();
}
