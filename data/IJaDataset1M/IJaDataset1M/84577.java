package org.fb4j.impl;

import junit.framework.Assert;
import org.fb4j.ErrorResponseException;
import org.fb4j.Session;
import org.junit.Test;

/**
 * @author Mino Togna
 * 
 */
public class IsAppUserTest extends SessionTestBase {

    @Test
    public void testIsAppUser() {
        Session session = getSession();
        Boolean isAppUser = session.isAppUser();
        Assert.assertNotNull(isAppUser);
        Assert.assertEquals(Boolean.class, isAppUser.getClass());
        log.debug(isAppUser);
        isAppUser = session.isAppUser(591307764L);
        Assert.assertNotNull(isAppUser);
        Assert.assertEquals(Boolean.class, isAppUser.getClass());
        log.debug(isAppUser);
    }

    public void testIsAppUserError() {
        Session session = getSession();
        Boolean isAppUser = session.isAppUser(752141196L);
        Assert.assertNotNull(isAppUser);
        Assert.assertEquals(Boolean.class, isAppUser.getClass());
        log.debug(isAppUser);
    }
}
