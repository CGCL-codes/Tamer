package com.liferay.portlet.announcements.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.service.persistence.BasePersistenceTestCase;
import com.liferay.portlet.announcements.NoSuchDeliveryException;
import com.liferay.portlet.announcements.model.AnnouncementsDelivery;

/**
 * <a href="AnnouncementsDeliveryPersistenceTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AnnouncementsDeliveryPersistenceTest extends BasePersistenceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        _persistence = (AnnouncementsDeliveryPersistence) PortalBeanLocatorUtil.locate(AnnouncementsDeliveryPersistence.class.getName() + ".impl");
    }

    public void testCreate() throws Exception {
        long pk = nextLong();
        AnnouncementsDelivery announcementsDelivery = _persistence.create(pk);
        assertNotNull(announcementsDelivery);
        assertEquals(announcementsDelivery.getPrimaryKey(), pk);
    }

    public void testRemove() throws Exception {
        AnnouncementsDelivery newAnnouncementsDelivery = addAnnouncementsDelivery();
        _persistence.remove(newAnnouncementsDelivery);
        AnnouncementsDelivery existingAnnouncementsDelivery = _persistence.fetchByPrimaryKey(newAnnouncementsDelivery.getPrimaryKey());
        assertNull(existingAnnouncementsDelivery);
    }

    public void testUpdateNew() throws Exception {
        addAnnouncementsDelivery();
    }

    public void testUpdateExisting() throws Exception {
        long pk = nextLong();
        AnnouncementsDelivery newAnnouncementsDelivery = _persistence.create(pk);
        newAnnouncementsDelivery.setCompanyId(nextLong());
        newAnnouncementsDelivery.setUserId(nextLong());
        newAnnouncementsDelivery.setType(randomString());
        newAnnouncementsDelivery.setEmail(randomBoolean());
        newAnnouncementsDelivery.setSms(randomBoolean());
        newAnnouncementsDelivery.setWebsite(randomBoolean());
        _persistence.update(newAnnouncementsDelivery, false);
        AnnouncementsDelivery existingAnnouncementsDelivery = _persistence.findByPrimaryKey(newAnnouncementsDelivery.getPrimaryKey());
        assertEquals(existingAnnouncementsDelivery.getDeliveryId(), newAnnouncementsDelivery.getDeliveryId());
        assertEquals(existingAnnouncementsDelivery.getCompanyId(), newAnnouncementsDelivery.getCompanyId());
        assertEquals(existingAnnouncementsDelivery.getUserId(), newAnnouncementsDelivery.getUserId());
        assertEquals(existingAnnouncementsDelivery.getType(), newAnnouncementsDelivery.getType());
        assertEquals(existingAnnouncementsDelivery.getEmail(), newAnnouncementsDelivery.getEmail());
        assertEquals(existingAnnouncementsDelivery.getSms(), newAnnouncementsDelivery.getSms());
        assertEquals(existingAnnouncementsDelivery.getWebsite(), newAnnouncementsDelivery.getWebsite());
    }

    public void testFindByPrimaryKeyExisting() throws Exception {
        AnnouncementsDelivery newAnnouncementsDelivery = addAnnouncementsDelivery();
        AnnouncementsDelivery existingAnnouncementsDelivery = _persistence.findByPrimaryKey(newAnnouncementsDelivery.getPrimaryKey());
        assertEquals(existingAnnouncementsDelivery, newAnnouncementsDelivery);
    }

    public void testFindByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        try {
            _persistence.findByPrimaryKey(pk);
            fail("Missing entity did not throw NoSuchDeliveryException");
        } catch (NoSuchDeliveryException nsee) {
        }
    }

    public void testFetchByPrimaryKeyExisting() throws Exception {
        AnnouncementsDelivery newAnnouncementsDelivery = addAnnouncementsDelivery();
        AnnouncementsDelivery existingAnnouncementsDelivery = _persistence.fetchByPrimaryKey(newAnnouncementsDelivery.getPrimaryKey());
        assertEquals(existingAnnouncementsDelivery, newAnnouncementsDelivery);
    }

    public void testFetchByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        AnnouncementsDelivery missingAnnouncementsDelivery = _persistence.fetchByPrimaryKey(pk);
        assertNull(missingAnnouncementsDelivery);
    }

    protected AnnouncementsDelivery addAnnouncementsDelivery() throws Exception {
        long pk = nextLong();
        AnnouncementsDelivery announcementsDelivery = _persistence.create(pk);
        announcementsDelivery.setCompanyId(nextLong());
        announcementsDelivery.setUserId(nextLong());
        announcementsDelivery.setType(randomString());
        announcementsDelivery.setEmail(randomBoolean());
        announcementsDelivery.setSms(randomBoolean());
        announcementsDelivery.setWebsite(randomBoolean());
        _persistence.update(announcementsDelivery, false);
        return announcementsDelivery;
    }

    private AnnouncementsDeliveryPersistence _persistence;
}
