package org.shams.phonebook.dao.item;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.shams.phonebook.dao.ItemDao;
import org.shams.phonebook.domain.impl.Item;
import org.shams.phonebook.utils.DataBaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import java.util.List;

/**
 * @version 0.0.0
 *          Date: Feb 12, 2008
 * @auther <a href="mailto:m.h.shams@gmail.com"> M. H. Shamsi </a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "txManager")
public abstract class BaseItemDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ItemDao dao;

    public void save() {
        int before = countRowsInTable("_item");
        dao.save(getItem());
        int after = countRowsInTable("_item");
        assertEquals(after, before + 1);
    }

    public void load() {
        int before = countRowsInTable("_item");
        Item item = getItem();
        dao.save(item);
        assertEquals(countRowsInTable("_item"), before + 1);
        List<Item> items = dao.load();
        assertEquals(items.size(), before + 1);
        assertEquals(items.get(before).getFirstName(), "f");
        Item lastItem = dao.load(items.get(before).getId());
        assertNotNull(lastItem);
        assertEquals(lastItem.getLastName(), "l");
        assertEquals(dao.load("@").size(), 1);
        assertEquals(dao.load("@1").size(), 0);
    }

    public void delete() {
        int before = countRowsInTable("_item");
        dao.save(getItem());
        List<Item> items = dao.load();
        assertEquals(before + 1, items.size());
        dao.delete(items.get(before));
        assertEquals(before, dao.load().size());
    }

    private Item getItem() {
        return new Item("f", "l", "p", "m", "@", "A");
    }
}
