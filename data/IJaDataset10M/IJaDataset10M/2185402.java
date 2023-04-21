package com.liferay.portlet.journal.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.service.persistence.BasePersistenceTestCase;
import com.liferay.portlet.journal.NoSuchArticleResourceException;
import com.liferay.portlet.journal.model.JournalArticleResource;

/**
 * <a href="JournalArticleResourcePersistenceTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class JournalArticleResourcePersistenceTest extends BasePersistenceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        _persistence = (JournalArticleResourcePersistence) PortalBeanLocatorUtil.locate(JournalArticleResourcePersistence.class.getName() + ".impl");
    }

    public void testCreate() throws Exception {
        long pk = nextLong();
        JournalArticleResource journalArticleResource = _persistence.create(pk);
        assertNotNull(journalArticleResource);
        assertEquals(journalArticleResource.getPrimaryKey(), pk);
    }

    public void testRemove() throws Exception {
        JournalArticleResource newJournalArticleResource = addJournalArticleResource();
        _persistence.remove(newJournalArticleResource);
        JournalArticleResource existingJournalArticleResource = _persistence.fetchByPrimaryKey(newJournalArticleResource.getPrimaryKey());
        assertNull(existingJournalArticleResource);
    }

    public void testUpdateNew() throws Exception {
        addJournalArticleResource();
    }

    public void testUpdateExisting() throws Exception {
        long pk = nextLong();
        JournalArticleResource newJournalArticleResource = _persistence.create(pk);
        newJournalArticleResource.setGroupId(nextLong());
        newJournalArticleResource.setArticleId(randomString());
        _persistence.update(newJournalArticleResource, false);
        JournalArticleResource existingJournalArticleResource = _persistence.findByPrimaryKey(newJournalArticleResource.getPrimaryKey());
        assertEquals(existingJournalArticleResource.getResourcePrimKey(), newJournalArticleResource.getResourcePrimKey());
        assertEquals(existingJournalArticleResource.getGroupId(), newJournalArticleResource.getGroupId());
        assertEquals(existingJournalArticleResource.getArticleId(), newJournalArticleResource.getArticleId());
    }

    public void testFindByPrimaryKeyExisting() throws Exception {
        JournalArticleResource newJournalArticleResource = addJournalArticleResource();
        JournalArticleResource existingJournalArticleResource = _persistence.findByPrimaryKey(newJournalArticleResource.getPrimaryKey());
        assertEquals(existingJournalArticleResource, newJournalArticleResource);
    }

    public void testFindByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        try {
            _persistence.findByPrimaryKey(pk);
            fail("Missing entity did not throw NoSuchArticleResourceException");
        } catch (NoSuchArticleResourceException nsee) {
        }
    }

    public void testFetchByPrimaryKeyExisting() throws Exception {
        JournalArticleResource newJournalArticleResource = addJournalArticleResource();
        JournalArticleResource existingJournalArticleResource = _persistence.fetchByPrimaryKey(newJournalArticleResource.getPrimaryKey());
        assertEquals(existingJournalArticleResource, newJournalArticleResource);
    }

    public void testFetchByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        JournalArticleResource missingJournalArticleResource = _persistence.fetchByPrimaryKey(pk);
        assertNull(missingJournalArticleResource);
    }

    protected JournalArticleResource addJournalArticleResource() throws Exception {
        long pk = nextLong();
        JournalArticleResource journalArticleResource = _persistence.create(pk);
        journalArticleResource.setGroupId(nextLong());
        journalArticleResource.setArticleId(randomString());
        _persistence.update(journalArticleResource, false);
        return journalArticleResource;
    }

    private JournalArticleResourcePersistence _persistence;
}
