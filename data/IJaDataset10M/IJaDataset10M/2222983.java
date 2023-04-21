package org.eclipse.mylyn.internal.jira.core.model;

import java.io.Serializable;

/**
 * TODO merge with IssueLink?
 * 
 * @author Eugene Kuleshov
 */
public class Subtask implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String issueId;

    private final String issueKey;

    public Subtask(String issueId, String issueKey) {
        this.issueId = issueId;
        this.issueKey = issueKey;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getIssueKey() {
        return this.issueKey;
    }
}
