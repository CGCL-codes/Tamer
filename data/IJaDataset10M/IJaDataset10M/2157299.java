package com.liferay.portlet.documentlibrary.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.service.persistence.BasePersistenceTestCase;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;

/**
 * <a href="DLFileEntryPersistenceTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class DLFileEntryPersistenceTest extends BasePersistenceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        _persistence = (DLFileEntryPersistence) PortalBeanLocatorUtil.locate(DLFileEntryPersistence.class.getName() + ".impl");
    }

    public void testCreate() throws Exception {
        long pk = nextLong();
        DLFileEntry dlFileEntry = _persistence.create(pk);
        assertNotNull(dlFileEntry);
        assertEquals(dlFileEntry.getPrimaryKey(), pk);
    }

    public void testRemove() throws Exception {
        DLFileEntry newDLFileEntry = addDLFileEntry();
        _persistence.remove(newDLFileEntry);
        DLFileEntry existingDLFileEntry = _persistence.fetchByPrimaryKey(newDLFileEntry.getPrimaryKey());
        assertNull(existingDLFileEntry);
    }

    public void testUpdateNew() throws Exception {
        addDLFileEntry();
    }

    public void testUpdateExisting() throws Exception {
        long pk = nextLong();
        DLFileEntry newDLFileEntry = _persistence.create(pk);
        newDLFileEntry.setUuid(randomString());
        newDLFileEntry.setCompanyId(nextLong());
        newDLFileEntry.setUserId(nextLong());
        newDLFileEntry.setUserName(randomString());
        newDLFileEntry.setVersionUserId(nextLong());
        newDLFileEntry.setVersionUserName(randomString());
        newDLFileEntry.setCreateDate(nextDate());
        newDLFileEntry.setModifiedDate(nextDate());
        newDLFileEntry.setFolderId(nextLong());
        newDLFileEntry.setName(randomString());
        newDLFileEntry.setTitle(randomString());
        newDLFileEntry.setDescription(randomString());
        newDLFileEntry.setVersion(nextDouble());
        newDLFileEntry.setSize(nextInt());
        newDLFileEntry.setReadCount(nextInt());
        newDLFileEntry.setExtraSettings(randomString());
        _persistence.update(newDLFileEntry, false);
        DLFileEntry existingDLFileEntry = _persistence.findByPrimaryKey(newDLFileEntry.getPrimaryKey());
        assertEquals(existingDLFileEntry.getUuid(), newDLFileEntry.getUuid());
        assertEquals(existingDLFileEntry.getFileEntryId(), newDLFileEntry.getFileEntryId());
        assertEquals(existingDLFileEntry.getCompanyId(), newDLFileEntry.getCompanyId());
        assertEquals(existingDLFileEntry.getUserId(), newDLFileEntry.getUserId());
        assertEquals(existingDLFileEntry.getUserName(), newDLFileEntry.getUserName());
        assertEquals(existingDLFileEntry.getVersionUserId(), newDLFileEntry.getVersionUserId());
        assertEquals(existingDLFileEntry.getVersionUserName(), newDLFileEntry.getVersionUserName());
        assertEquals(existingDLFileEntry.getCreateDate(), newDLFileEntry.getCreateDate());
        assertEquals(existingDLFileEntry.getModifiedDate(), newDLFileEntry.getModifiedDate());
        assertEquals(existingDLFileEntry.getFolderId(), newDLFileEntry.getFolderId());
        assertEquals(existingDLFileEntry.getName(), newDLFileEntry.getName());
        assertEquals(existingDLFileEntry.getTitle(), newDLFileEntry.getTitle());
        assertEquals(existingDLFileEntry.getDescription(), newDLFileEntry.getDescription());
        assertEquals(existingDLFileEntry.getVersion(), newDLFileEntry.getVersion());
        assertEquals(existingDLFileEntry.getSize(), newDLFileEntry.getSize());
        assertEquals(existingDLFileEntry.getReadCount(), newDLFileEntry.getReadCount());
        assertEquals(existingDLFileEntry.getExtraSettings(), newDLFileEntry.getExtraSettings());
    }

    public void testFindByPrimaryKeyExisting() throws Exception {
        DLFileEntry newDLFileEntry = addDLFileEntry();
        DLFileEntry existingDLFileEntry = _persistence.findByPrimaryKey(newDLFileEntry.getPrimaryKey());
        assertEquals(existingDLFileEntry, newDLFileEntry);
    }

    public void testFindByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        try {
            _persistence.findByPrimaryKey(pk);
            fail("Missing entity did not throw NoSuchFileEntryException");
        } catch (NoSuchFileEntryException nsee) {
        }
    }

    public void testFetchByPrimaryKeyExisting() throws Exception {
        DLFileEntry newDLFileEntry = addDLFileEntry();
        DLFileEntry existingDLFileEntry = _persistence.fetchByPrimaryKey(newDLFileEntry.getPrimaryKey());
        assertEquals(existingDLFileEntry, newDLFileEntry);
    }

    public void testFetchByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        DLFileEntry missingDLFileEntry = _persistence.fetchByPrimaryKey(pk);
        assertNull(missingDLFileEntry);
    }

    protected DLFileEntry addDLFileEntry() throws Exception {
        long pk = nextLong();
        DLFileEntry dlFileEntry = _persistence.create(pk);
        dlFileEntry.setUuid(randomString());
        dlFileEntry.setCompanyId(nextLong());
        dlFileEntry.setUserId(nextLong());
        dlFileEntry.setUserName(randomString());
        dlFileEntry.setVersionUserId(nextLong());
        dlFileEntry.setVersionUserName(randomString());
        dlFileEntry.setCreateDate(nextDate());
        dlFileEntry.setModifiedDate(nextDate());
        dlFileEntry.setFolderId(nextLong());
        dlFileEntry.setName(randomString());
        dlFileEntry.setTitle(randomString());
        dlFileEntry.setDescription(randomString());
        dlFileEntry.setVersion(nextDouble());
        dlFileEntry.setSize(nextInt());
        dlFileEntry.setReadCount(nextInt());
        dlFileEntry.setExtraSettings(randomString());
        _persistence.update(dlFileEntry, false);
        return dlFileEntry;
    }

    private DLFileEntryPersistence _persistence;
}
