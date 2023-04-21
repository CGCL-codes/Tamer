package com.liferay.ruon.service;

import com.liferay.portal.service.BaseServiceTestCase;
import com.liferay.ruon.NoSuchUserPresenceException;
import com.liferay.ruon.model.UserPresence;
import com.liferay.ruon.model.impl.UserPresenceImpl;
import com.liferay.ruon.util.RUONException;

/**
 * <a href="BaseRUONTestCase.java.html"><b><i>View Source</i>
 * </b></a>
 *
 * @author Murali Krishna Reddy
 *
 */
public class BaseRUONTestCase extends BaseServiceTestCase {

    public void setUp() throws Exception {
        super.setUp();
        try {
            UserPresence pUser = UserPresenceLocalServiceUtil.getUserPresence(userId);
            pUser.setPresenceStatus(1);
            UserPresenceLocalServiceUtil.updateUserPresence(pUser);
        } catch (NoSuchUserPresenceException e) {
            UserPresence pUserNew = new UserPresenceImpl();
            pUserNew.setPresenceUserId(userId);
            pUserNew.setPresenceStatus(2);
            try {
                UserPresenceLocalServiceUtil.addUserPresence(pUserNew);
            } catch (Exception se) {
                throw new RUONException(se);
            }
        } catch (Exception e) {
            throw new RUONException(e);
        }
    }

    public void tearDown() throws Exception {
        UserPresenceLocalServiceUtil.deleteUserPresence(userId);
        super.tearDown();
    }

    protected Long userId = new Long(9876);
}
