package com.avaje.tests.compositeKeys;

import java.util.List;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.tests.compositeKeys.db.Item;
import com.avaje.tests.compositeKeys.db.ItemKey;
import com.avaje.tests.compositeKeys.db.Region;
import com.avaje.tests.compositeKeys.db.RegionKey;
import com.avaje.tests.compositeKeys.db.Type;
import com.avaje.tests.compositeKeys.db.TypeKey;
import com.avaje.tests.lib.EbeanTestCase;

/**
 * Test some of the Avaje core functionality in conjunction with composite keys like
 * <ul>
 * <li>write</li>
 * <li>find</li>
 * </ul>
 */
public class TestCore extends EbeanTestCase {

    @Override
    public void setUp() throws Exception {
        Transaction tx = getServer().beginTransaction();
        Type type = new Type();
        TypeKey typeKey = new TypeKey();
        typeKey.setCustomer(1);
        typeKey.setType(10);
        type.setKey(typeKey);
        type.setDescription("Type Old-Item - Customer 1");
        getServer().save(type);
        type = new Type();
        typeKey = new TypeKey();
        typeKey.setCustomer(2);
        typeKey.setType(10);
        type.setKey(typeKey);
        type.setDescription("Type Old-Item - Customer 2");
        getServer().save(type);
        Region region = new Region();
        RegionKey regionKey = new RegionKey();
        regionKey.setCustomer(1);
        regionKey.setType(500);
        region.setKey(regionKey);
        region.setDescription("Region West - Customer 1");
        getServer().save(region);
        region = new Region();
        regionKey = new RegionKey();
        regionKey.setCustomer(2);
        regionKey.setType(500);
        region.setKey(regionKey);
        region.setDescription("Region West - Customer 2");
        getServer().save(region);
        Item item = new Item();
        ItemKey itemKey = new ItemKey();
        itemKey.setCustomer(1);
        itemKey.setItemNumber("ITEM1");
        item.setKey(itemKey);
        item.setDescription("Fancy Car - Customer 1");
        item.setRegion(500);
        item.setType(10);
        getServer().save(item);
        item = new Item();
        itemKey = new ItemKey();
        itemKey.setCustomer(2);
        itemKey.setItemNumber("ITEM1");
        item.setKey(itemKey);
        item.setDescription("Another Fancy Car - Customer 2");
        item.setRegion(500);
        item.setType(10);
        getServer().save(item);
        tx.commit();
    }

    public void testFind() {
        List<Item> items = getServer().find(Item.class).findList();
        assertNotNull(items);
        assertEquals(2, items.size());
        Query<Item> qItems = getServer().find(Item.class);
        qItems.where().eq("key.customer", Integer.valueOf(1));
        items = qItems.findList();
        assertNotNull(items);
        assertEquals(1, items.size());
    }
}
