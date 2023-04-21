package com.reserveamerica.jirarmi.beans.issue.link;

import com.reserveamerica.jirarmi.beans.EntityRemote;

public class IssueLinkRemote extends EntityRemote {

    private static final long serialVersionUID = -3696123708751778643L;

    private Long issueLinkTypeId;

    private Long sourceIssueId;

    private Long destinationIssueId;

    private Long sequence;

    private boolean subtask;

    private boolean system;

    public IssueLinkRemote(Long id) {
        super(id);
    }

    public Long getIssueLinkTypeId() {
        return issueLinkTypeId;
    }

    public void setIssueLinkTypeId(Long issueLinkTypeId) {
        this.issueLinkTypeId = issueLinkTypeId;
    }

    /**
   * Get the Issue ID of the linked-from issue.
   */
    public Long getSourceIssueId() {
        return sourceIssueId;
    }

    public void setSourceIssueId(Long sourceIssueId) {
        this.sourceIssueId = sourceIssueId;
    }

    /**
   * Get the issue ID of the linked-to issue.
   */
    public Long getDestinationIssueId() {
        return destinationIssueId;
    }

    public void setDestinationIssueId(Long destinationIssueId) {
        this.destinationIssueId = destinationIssueId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public boolean isSubtaskLink() {
        return subtask;
    }

    public void setSubTaskLink(boolean subtask) {
        this.subtask = subtask;
    }

    /**
   * Checks if this link's type is a System Link type. A System Link Type is a link type
   * that is used by JIRA to denote special relationships. For example, a sub-task is linked to its
   * parent issue using a link type that is a System Link Type.
   */
    public boolean isSystemLink() {
        return system;
    }

    public void setSystemLink(boolean system) {
        this.system = system;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(super.toString());
        sb.append(", issueLinkTypeId: ").append(issueLinkTypeId);
        sb.append(", sourceIssueId: ").append(sourceIssueId);
        sb.append(", destinationIssueId: ").append(destinationIssueId);
        sb.append(", sequence: ").append(sequence);
        sb.append(", subtask: ").append(subtask);
        sb.append(", system: ").append(system);
        sb.append("]");
        return sb.toString();
    }
}
