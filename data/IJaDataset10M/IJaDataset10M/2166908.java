package net.sf.l2j.gameserver.templates;

import net.sf.l2j.gameserver.model.L2ItemInstance;

/**
 * This class contains L2ItemInstance<BR>
 * Use to sort L2ItemInstance of : <LI>L2Armor</LI> <LI>L2EtcItem</LI> <LI>L2Weapon</LI>
 *
 * @version $Revision: 1.7.2.2.2.5 $ $Date: 2005/04/06 18:25:18 $
 */
public class L2WarehouseItem {

    private L2Item _item;

    private int _object;

    private int _count;

    private int _owner;

    private int _enchant;

    private int _grade;

    private boolean _isAugmented;

    private int _augmentationId;

    public L2WarehouseItem(L2ItemInstance item) {
        _item = item.getItem();
        _object = item.getObjectId();
        _count = item.getCount();
        _owner = item.getOwnerId();
        _enchant = item.getEnchantLevel();
        _grade = item.getItem().getItemGrade();
        if (item.isAugmented()) {
            _isAugmented = true;
            _augmentationId = item.getAugmentation().getAugmentationId();
        } else _isAugmented = false;
    }

    /**
	 * Returns the item.
	 *
	 * @return L2Item
	 */
    public L2Item getItem() {
        return _item;
    }

    /**
	 * Returns the unique objectId
	 *
	 * @return int
	 */
    public final int getObjectId() {
        return _object;
    }

    /**
	 * Returns the owner
	 *
	 * @return int
	 */
    public final int getOwnerId() {
        return _owner;
    }

    /**
	 * Returns the count
	 *
	 * @return int
	 */
    public final int getCount() {
        return _count;
    }

    /**
	 * Returns the first type
	 *
	 * @return int
	 */
    public final int getType1() {
        return _item.getType1();
    }

    /**
	 * Returns the second type
	 *
	 * @return int
	 */
    public final int getType2() {
        return _item.getType2();
    }

    /**
	 * Returns the second type
	 *
	 * @return int
	 */
    @SuppressWarnings("unchecked")
    public final Enum getItemType() {
        return _item.getItemType();
    }

    /**
	 * Returns the ItemId
	 *
	 * @return int
	 */
    public final int getItemId() {
        return _item.getItemId();
    }

    /**
	 * Returns the part of body used with this item
	 *
	 * @return int
	 */
    public final int getBodyPart() {
        return _item.getBodyPart();
    }

    /**
	 * Returns the enchant level
	 *
	 * @return int
	 */
    public final int getEnchantLevel() {
        return _enchant;
    }

    /**
	 * Returns the item grade
	 *
	 * @return int
	 */
    public final int getItemGrade() {
        return _grade;
    }

    /**
	 * Returns true if it is a weapon
	 *
	 * @return boolean
	 */
    public final boolean isWeapon() {
        return _item instanceof L2Weapon;
    }

    /**
	 * Returns true if it is an armor
	 *
	 * @return boolean
	 */
    public final boolean isArmor() {
        return _item instanceof L2Armor;
    }

    /**
	 * Returns true if it is an EtcItem
	 *
	 * @return boolean
	 */
    public final boolean isEtcItem() {
        return _item instanceof L2EtcItem;
    }

    /**
	 * Returns the name of the item
	 *
	 * @return String
	 */
    public String getItemName() {
        return _item.getName();
    }

    public boolean isAugmented() {
        return _isAugmented;
    }

    public int getAugmentationId() {
        return _augmentationId;
    }

    /**
	 * Returns the name of the item
	 *
	 * @return String
	 * @deprecated beware to use getItemName() instead because getName() is final in L2Object and could not be overriden! Allover L2Object.getName() may return null!
	 */
    @Deprecated
    public String getName() {
        return _item.getName();
    }

    /**
	 * Returns the name of the item
	 *
	 * @return String
	 */
    @Override
    public String toString() {
        return _item.toString();
    }
}
