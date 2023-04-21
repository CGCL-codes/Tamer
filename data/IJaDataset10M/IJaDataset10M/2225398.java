package org.apache.shiro.session.mgt;

/**
 * Interface that should be implemented by classes that can control validating sessions on a regular
 * basis.  This interface is used as a delegate for session validation by the {@link org.apache.shiro.session.mgt.DefaultSessionManager}
 *
 * @see org.apache.shiro.session.mgt.DefaultSessionManager#setSessionValidationScheduler(SessionValidationScheduler)
 * @since 0.1
 */
public interface SessionValidationScheduler {

    /**
     * Returns <code>true</code> if this Scheduler is enabled and ready to begin validation at the appropriate time,
     * <code>false</code> otherwise.
     * <p/>
     * It does <em>not</em> indicate if the validation is actually executing at that instant - only that it is prepared
     * to do so at the appropriate time.
     *
     * @return <code>true</code> if this Scheduler is enabled and ready to begin validation at the appropriate time,
     * <code>false</code> otherwise.
     */
    boolean isEnabled();

    /**
     * Enables the session validation job.
     */
    void enableSessionValidation();

    /**
     * Disables the session validation job.
     */
    void disableSessionValidation();
}
