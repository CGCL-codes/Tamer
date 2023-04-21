package com.liferay.portlet.messageboards.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.service.persistence.BasePersistenceTestCase;
import com.liferay.portlet.messageboards.NoSuchDiscussionException;
import com.liferay.portlet.messageboards.model.MBDiscussion;

/**
 * <a href="MBDiscussionPersistenceTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class MBDiscussionPersistenceTest extends BasePersistenceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        _persistence = (MBDiscussionPersistence) PortalBeanLocatorUtil.locate(MBDiscussionPersistence.class.getName() + ".impl");
    }

    public void testCreate() throws Exception {
        long pk = nextLong();
        MBDiscussion mbDiscussion = _persistence.create(pk);
        assertNotNull(mbDiscussion);
        assertEquals(mbDiscussion.getPrimaryKey(), pk);
    }

    public void testRemove() throws Exception {
        MBDiscussion newMBDiscussion = addMBDiscussion();
        _persistence.remove(newMBDiscussion);
        MBDiscussion existingMBDiscussion = _persistence.fetchByPrimaryKey(newMBDiscussion.getPrimaryKey());
        assertNull(existingMBDiscussion);
    }

    public void testUpdateNew() throws Exception {
        addMBDiscussion();
    }

    public void testUpdateExisting() throws Exception {
        long pk = nextLong();
        MBDiscussion newMBDiscussion = _persistence.create(pk);
        newMBDiscussion.setClassNameId(nextLong());
        newMBDiscussion.setClassPK(nextLong());
        newMBDiscussion.setThreadId(nextLong());
        _persistence.update(newMBDiscussion, false);
        MBDiscussion existingMBDiscussion = _persistence.findByPrimaryKey(newMBDiscussion.getPrimaryKey());
        assertEquals(existingMBDiscussion.getDiscussionId(), newMBDiscussion.getDiscussionId());
        assertEquals(existingMBDiscussion.getClassNameId(), newMBDiscussion.getClassNameId());
        assertEquals(existingMBDiscussion.getClassPK(), newMBDiscussion.getClassPK());
        assertEquals(existingMBDiscussion.getThreadId(), newMBDiscussion.getThreadId());
    }

    public void testFindByPrimaryKeyExisting() throws Exception {
        MBDiscussion newMBDiscussion = addMBDiscussion();
        MBDiscussion existingMBDiscussion = _persistence.findByPrimaryKey(newMBDiscussion.getPrimaryKey());
        assertEquals(existingMBDiscussion, newMBDiscussion);
    }

    public void testFindByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        try {
            _persistence.findByPrimaryKey(pk);
            fail("Missing entity did not throw NoSuchDiscussionException");
        } catch (NoSuchDiscussionException nsee) {
        }
    }

    public void testFetchByPrimaryKeyExisting() throws Exception {
        MBDiscussion newMBDiscussion = addMBDiscussion();
        MBDiscussion existingMBDiscussion = _persistence.fetchByPrimaryKey(newMBDiscussion.getPrimaryKey());
        assertEquals(existingMBDiscussion, newMBDiscussion);
    }

    public void testFetchByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        MBDiscussion missingMBDiscussion = _persistence.fetchByPrimaryKey(pk);
        assertNull(missingMBDiscussion);
    }

    protected MBDiscussion addMBDiscussion() throws Exception {
        long pk = nextLong();
        MBDiscussion mbDiscussion = _persistence.create(pk);
        mbDiscussion.setClassNameId(nextLong());
        mbDiscussion.setClassPK(nextLong());
        mbDiscussion.setThreadId(nextLong());
        _persistence.update(mbDiscussion, false);
        return mbDiscussion;
    }

    private MBDiscussionPersistence _persistence;
}
