package org.freelords.army.heroes;

import static org.junit.Assert.*;
import java.io.File;
import org.freelords.xmlmanager.XmlManager;
import org.freelords.xmlmanager.utils.XmlTestHelper;
import org.junit.Before;
import org.junit.Test;

public class BackPackTest {

    private File itemFile = new File(this.getClass().getResource("files/item.xml").getFile());

    private Item item;

    private Backpack testBackPack;

    private Equipment equipment;

    @Before
    public void setUp() throws Exception {
        XmlManager itemManager = XmlTestHelper.createXmlManagerFor(Item.class);
        this.item = itemManager.unmarshall(this.itemFile);
        this.testBackPack = new Backpack();
        this.equipment = new Equipment();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveItemThrowsExceptionIfItemNotInBackPack() {
        testBackPack.removeItem(item);
    }

    @Test
    public void testStoreAndRemoveItem() {
        testBackPack.storeItem(item);
        testBackPack.removeItem(item);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removingCleansTheBackpack() {
        testBackPack.storeItem(item);
        testBackPack.removeItem(item);
        testBackPack.removeItem(item);
    }

    @Test
    public void testEquipItem() {
        setupEquipment();
        Item removedItem = equipment.remove(item.getType());
        assertEquals("Items are not equal!", removedItem, item);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEquipItemNotInBackPack() {
        testBackPack.equipItem(equipment, item);
    }

    @Test
    public void testTakeOffItem() {
        setupEquipment();
        testBackPack.takeOffItem(equipment, item.getType());
        assertNull("Item not removed from equipment!", equipment.remove(item.getType()));
        testBackPack.removeItem(item);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTakeOffItemWhenItemNotEquipped() {
        testBackPack.takeOffItem(equipment, ItemType.ARMOR);
    }

    private void setupEquipment() {
        testBackPack.storeItem(item);
        testBackPack.equipItem(equipment, item);
    }
}
