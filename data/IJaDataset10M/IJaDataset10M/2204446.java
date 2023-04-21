package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.AbstractTransactionalDaoTestCase;
import org.opennms.netmgt.model.inventory.OnmsInventoryCategory;

public class InventoryCategoryDaoHibernateTest extends AbstractTransactionalDaoTestCase {

    public void testInitialize() {
    }

    public void testSaveOnmsInventoryCategory() {
        OnmsInventoryCategory invCat = new OnmsInventoryCategory("Example Category");
        getInventoryCategoryDao().save(invCat);
        getInventoryCategoryDao().flush();
        getInventoryCategoryDao().clear();
        Object[] args = { invCat.getId() };
        assertEquals(1, getJdbcTemplate().queryForInt("select count(*) from inventorycategory where id = ?", args));
        OnmsInventoryCategory invCat2 = getInventoryCategoryDao().findCategoryId(invCat.getId());
        assertNotSame(invCat, invCat2);
        assertEquals(invCat.getCategoryId(), invCat2.getCategoryId());
        assertEquals(invCat.getCategoryName(), invCat2.getCategoryName());
    }

    public void testFindByName() {
        OnmsInventoryCategory invCat = getInventoryCategoryDao().findByName("Test Category");
        assertEquals("Test Category", invCat.getCategoryName());
    }
}
