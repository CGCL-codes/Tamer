package org.skycastle.util.issue;

/**
 * A listener that can recieve various error, warning and debug reports, in the form of {@link Issue}s.
 *
 * @author Hans Haggstrom
 */
public interface IssueListener {

    /**
     * Called when there is a new {@link Issue}�.
     *
     * @param issue a description of the issue.  Should not be null.
     */
    void onIssue(Issue issue);
}
