package com.liferay.portlet.shopping.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.service.persistence.BasePersistenceTestCase;
import com.liferay.portlet.shopping.NoSuchCategoryException;
import com.liferay.portlet.shopping.model.ShoppingCategory;

/**
 * <a href="ShoppingCategoryPersistenceTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ShoppingCategoryPersistenceTest extends BasePersistenceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        _persistence = (ShoppingCategoryPersistence) PortalBeanLocatorUtil.locate(ShoppingCategoryPersistence.class.getName() + ".impl");
    }

    public void testCreate() throws Exception {
        long pk = nextLong();
        ShoppingCategory shoppingCategory = _persistence.create(pk);
        assertNotNull(shoppingCategory);
        assertEquals(shoppingCategory.getPrimaryKey(), pk);
    }

    public void testRemove() throws Exception {
        ShoppingCategory newShoppingCategory = addShoppingCategory();
        _persistence.remove(newShoppingCategory);
        ShoppingCategory existingShoppingCategory = _persistence.fetchByPrimaryKey(newShoppingCategory.getPrimaryKey());
        assertNull(existingShoppingCategory);
    }

    public void testUpdateNew() throws Exception {
        addShoppingCategory();
    }

    public void testUpdateExisting() throws Exception {
        long pk = nextLong();
        ShoppingCategory newShoppingCategory = _persistence.create(pk);
        newShoppingCategory.setGroupId(nextLong());
        newShoppingCategory.setCompanyId(nextLong());
        newShoppingCategory.setUserId(nextLong());
        newShoppingCategory.setUserName(randomString());
        newShoppingCategory.setCreateDate(nextDate());
        newShoppingCategory.setModifiedDate(nextDate());
        newShoppingCategory.setParentCategoryId(nextLong());
        newShoppingCategory.setName(randomString());
        newShoppingCategory.setDescription(randomString());
        _persistence.update(newShoppingCategory, false);
        ShoppingCategory existingShoppingCategory = _persistence.findByPrimaryKey(newShoppingCategory.getPrimaryKey());
        assertEquals(existingShoppingCategory.getCategoryId(), newShoppingCategory.getCategoryId());
        assertEquals(existingShoppingCategory.getGroupId(), newShoppingCategory.getGroupId());
        assertEquals(existingShoppingCategory.getCompanyId(), newShoppingCategory.getCompanyId());
        assertEquals(existingShoppingCategory.getUserId(), newShoppingCategory.getUserId());
        assertEquals(existingShoppingCategory.getUserName(), newShoppingCategory.getUserName());
        assertEquals(existingShoppingCategory.getCreateDate(), newShoppingCategory.getCreateDate());
        assertEquals(existingShoppingCategory.getModifiedDate(), newShoppingCategory.getModifiedDate());
        assertEquals(existingShoppingCategory.getParentCategoryId(), newShoppingCategory.getParentCategoryId());
        assertEquals(existingShoppingCategory.getName(), newShoppingCategory.getName());
        assertEquals(existingShoppingCategory.getDescription(), newShoppingCategory.getDescription());
    }

    public void testFindByPrimaryKeyExisting() throws Exception {
        ShoppingCategory newShoppingCategory = addShoppingCategory();
        ShoppingCategory existingShoppingCategory = _persistence.findByPrimaryKey(newShoppingCategory.getPrimaryKey());
        assertEquals(existingShoppingCategory, newShoppingCategory);
    }

    public void testFindByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        try {
            _persistence.findByPrimaryKey(pk);
            fail("Missing entity did not throw NoSuchCategoryException");
        } catch (NoSuchCategoryException nsee) {
        }
    }

    public void testFetchByPrimaryKeyExisting() throws Exception {
        ShoppingCategory newShoppingCategory = addShoppingCategory();
        ShoppingCategory existingShoppingCategory = _persistence.fetchByPrimaryKey(newShoppingCategory.getPrimaryKey());
        assertEquals(existingShoppingCategory, newShoppingCategory);
    }

    public void testFetchByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        ShoppingCategory missingShoppingCategory = _persistence.fetchByPrimaryKey(pk);
        assertNull(missingShoppingCategory);
    }

    protected ShoppingCategory addShoppingCategory() throws Exception {
        long pk = nextLong();
        ShoppingCategory shoppingCategory = _persistence.create(pk);
        shoppingCategory.setGroupId(nextLong());
        shoppingCategory.setCompanyId(nextLong());
        shoppingCategory.setUserId(nextLong());
        shoppingCategory.setUserName(randomString());
        shoppingCategory.setCreateDate(nextDate());
        shoppingCategory.setModifiedDate(nextDate());
        shoppingCategory.setParentCategoryId(nextLong());
        shoppingCategory.setName(randomString());
        shoppingCategory.setDescription(randomString());
        _persistence.update(shoppingCategory, false);
        return shoppingCategory;
    }

    private ShoppingCategoryPersistence _persistence;
}
