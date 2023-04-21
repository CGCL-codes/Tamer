package com.fddtool.si.context;

import javax.security.auth.Subject;

/**
 * A class implements <code>ISubjectContext</code> interface if it knows how
 * to store a subject of current user in a multi-user environment.
 *
 * @author Serguei Khramtchenko
 */
public interface ISubjectContext {

    /**
     * Returns the subject for the current user.
     *
     * @return Subject or <code>null</code> if the subject cannot be found.
     *     This may happen if user is not logged in.
     */
    public Subject getCurrentSubject();

    /**
     * Returns name of the user that was authenticated by container.
     * If the container did not authenticate the user - this method returns
     * <code>null</code>.
     *
     * @return String with user name, or <code>null</code>
     */
    public String getContainerUserName();

    /**
     * Sets the subject for the current user. This method is usually called
     * right after login.
     *
     * @param subject Subject that represents logged user.
     */
    public void setCurrentSubject(Subject subject);
}
