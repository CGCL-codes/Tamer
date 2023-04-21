package org.acegisecurity.providers.dao.salt;

import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.userdetails.UserDetails;
import org.springframework.beans.factory.InitializingBean;
import java.lang.reflect.Method;

/**
 * Obtains a salt from a specified property of the {@link User} object.
 * 
 * <P>
 * This allows you to subclass <code>User</code> and provide an additional bean
 * getter for a salt. You should use a synthetic value that does not change,
 * such as a database primary key.  Do not use <code>username</code> if it is
 * likely to change.
 * </p>
 *
 * @author Ben Alex
 * @version $Id: ReflectionSaltSource.java,v 1.8 2006/01/27 05:17:13 benalex Exp $
 */
public class ReflectionSaltSource implements SaltSource, InitializingBean {

    private String userPropertyToUse;

    public void afterPropertiesSet() throws Exception {
        if ((this.getUserPropertyToUse() == null) || "".equals(this.getUserPropertyToUse())) {
            throw new IllegalArgumentException("A userPropertyToUse must be set");
        }
    }

    /**
     * Performs reflection on the passed <code>User</code> to obtain the salt.
     * 
     * <P>
     * The property identified by <code>userPropertyToUse</code> must be
     * available from the passed <code>User</code> object. If it is not
     * available, an {@link AuthenticationServiceException} will be thrown.
     * </p>
     *
     * @param user which contains the method identified by
     *        <code>userPropertyToUse</code>
     *
     * @return the result of invoking <code>user.userPropertyToUse()</code>
     *
     * @throws AuthenticationServiceException if reflection fails
     */
    public Object getSalt(UserDetails user) {
        try {
            Method reflectionMethod = user.getClass().getMethod(this.userPropertyToUse, new Class[] {});
            return reflectionMethod.invoke(user, new Object[] {});
        } catch (Exception exception) {
            throw new AuthenticationServiceException(exception.getMessage(), exception);
        }
    }

    public String getUserPropertyToUse() {
        return userPropertyToUse;
    }

    /**
     * The method name to call to obtain the salt. If your
     * <code>UserDetails</code> contains a <code>UserDetails.getSalt()</code>
     * method, you should set this property to <code>getSalt</code>.
     *
     * @param userPropertyToUse the name of the <b>getter</b> to call to obtain
     *        the salt from the <code>UserDetails</code>
     */
    public void setUserPropertyToUse(String userPropertyToUse) {
        this.userPropertyToUse = userPropertyToUse;
    }
}
