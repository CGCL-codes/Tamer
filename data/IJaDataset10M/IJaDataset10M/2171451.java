package org.gtugs.service.security;

/**
 * @author jasonacooper@google.com (Jason Cooper)
 */
public interface AccessManager {

    public boolean hasAccess();

    public void setUsersService(UserService us);
}
