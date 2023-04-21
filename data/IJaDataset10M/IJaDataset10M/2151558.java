package org.eclipse.mylyn.internal.jira.core.service;

/**
 * Indicates an error during repository access.
 * 
 * @author Steffen Pingel
 */
public class JiraException extends Exception {

    private static final long serialVersionUID = -4354184850277873071L;

    public JiraException() {
    }

    public JiraException(String message, Throwable cause) {
        super(message, cause);
    }

    public JiraException(String message) {
        super(message);
    }

    public JiraException(Throwable cause) {
        super(cause);
    }
}
