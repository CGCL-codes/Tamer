package com.liferay.portlet.messageboards.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.service.persistence.BasePersistenceTestCase;
import com.liferay.portlet.messageboards.NoSuchStatsUserException;
import com.liferay.portlet.messageboards.model.MBStatsUser;

/**
 * <a href="MBStatsUserPersistenceTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class MBStatsUserPersistenceTest extends BasePersistenceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        _persistence = (MBStatsUserPersistence) PortalBeanLocatorUtil.locate(MBStatsUserPersistence.class.getName() + ".impl");
    }

    public void testCreate() throws Exception {
        long pk = nextLong();
        MBStatsUser mbStatsUser = _persistence.create(pk);
        assertNotNull(mbStatsUser);
        assertEquals(mbStatsUser.getPrimaryKey(), pk);
    }

    public void testRemove() throws Exception {
        MBStatsUser newMBStatsUser = addMBStatsUser();
        _persistence.remove(newMBStatsUser);
        MBStatsUser existingMBStatsUser = _persistence.fetchByPrimaryKey(newMBStatsUser.getPrimaryKey());
        assertNull(existingMBStatsUser);
    }

    public void testUpdateNew() throws Exception {
        addMBStatsUser();
    }

    public void testUpdateExisting() throws Exception {
        long pk = nextLong();
        MBStatsUser newMBStatsUser = _persistence.create(pk);
        newMBStatsUser.setGroupId(nextLong());
        newMBStatsUser.setUserId(nextLong());
        newMBStatsUser.setMessageCount(nextInt());
        newMBStatsUser.setLastPostDate(nextDate());
        _persistence.update(newMBStatsUser, false);
        MBStatsUser existingMBStatsUser = _persistence.findByPrimaryKey(newMBStatsUser.getPrimaryKey());
        assertEquals(existingMBStatsUser.getStatsUserId(), newMBStatsUser.getStatsUserId());
        assertEquals(existingMBStatsUser.getGroupId(), newMBStatsUser.getGroupId());
        assertEquals(existingMBStatsUser.getUserId(), newMBStatsUser.getUserId());
        assertEquals(existingMBStatsUser.getMessageCount(), newMBStatsUser.getMessageCount());
        assertEquals(existingMBStatsUser.getLastPostDate(), newMBStatsUser.getLastPostDate());
    }

    public void testFindByPrimaryKeyExisting() throws Exception {
        MBStatsUser newMBStatsUser = addMBStatsUser();
        MBStatsUser existingMBStatsUser = _persistence.findByPrimaryKey(newMBStatsUser.getPrimaryKey());
        assertEquals(existingMBStatsUser, newMBStatsUser);
    }

    public void testFindByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        try {
            _persistence.findByPrimaryKey(pk);
            fail("Missing entity did not throw NoSuchStatsUserException");
        } catch (NoSuchStatsUserException nsee) {
        }
    }

    public void testFetchByPrimaryKeyExisting() throws Exception {
        MBStatsUser newMBStatsUser = addMBStatsUser();
        MBStatsUser existingMBStatsUser = _persistence.fetchByPrimaryKey(newMBStatsUser.getPrimaryKey());
        assertEquals(existingMBStatsUser, newMBStatsUser);
    }

    public void testFetchByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        MBStatsUser missingMBStatsUser = _persistence.fetchByPrimaryKey(pk);
        assertNull(missingMBStatsUser);
    }

    protected MBStatsUser addMBStatsUser() throws Exception {
        long pk = nextLong();
        MBStatsUser mbStatsUser = _persistence.create(pk);
        mbStatsUser.setGroupId(nextLong());
        mbStatsUser.setUserId(nextLong());
        mbStatsUser.setMessageCount(nextInt());
        mbStatsUser.setLastPostDate(nextDate());
        _persistence.update(mbStatsUser, false);
        return mbStatsUser;
    }

    private MBStatsUserPersistence _persistence;
}
