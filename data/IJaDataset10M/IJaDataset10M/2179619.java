package org.helianto.core.filter.classic;

import org.helianto.core.User;

/**
 * Interface to <code>User</code> backed filters.
 * 
 * @author Mauricio Fernandes de Castro
 * @deprecated see AbstractPersonalFilterAdapter
 */
public interface UserBackedFilter extends EntityBackedFilter {

    /**
     * <code>User</code> getter.
     * @return
     */
    public User getUser();

    /**
     * <code>User</code> setter.
     * @param user
     */
    public void setUser(User user);
}
