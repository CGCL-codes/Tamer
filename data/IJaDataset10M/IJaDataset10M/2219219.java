package org.fb4j.impl;

import junit.framework.Assert;
import org.fb4j.ErrorResponseException;
import org.fb4j.Session;
import org.junit.Test;

/**
 * @author Mino Togna
 * 
 */
public class IsAppAddedToPageTest extends SessionTestBase {

    public void testIsAppAddedToPage() {
        Session session = getSession();
        Boolean isAppAddedToPage = session.isAppAddedToPage();
        log.debug(isAppAddedToPage);
    }

    @Test
    public void testIsAppAddedToPage1() {
        Session session = getSession();
        Boolean isAppAddedToPage = session.isAppAddedToPage(6262688677L);
        Assert.assertEquals(Boolean.TRUE, isAppAddedToPage);
        log.debug(isAppAddedToPage);
        isAppAddedToPage = session.isAppAddedToPage(9155126100L);
        Assert.assertEquals(Boolean.TRUE, isAppAddedToPage);
        log.debug(isAppAddedToPage);
    }
}
