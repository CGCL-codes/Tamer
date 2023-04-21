package odrop.shared.dto;

import odrop.shared.enumerators.ItemLocation;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * ItemOnCharacterDTO holds information about the item on the character.
 * In particular I meant to stress the contrast between ItemDTO and ItemOnCharacterDTO.
 * Objects of this class contain information about the amount, location, owner etc. of the item,
 * while an ItemDTO contains information about the item itself.
 * 
 * @author divStar
 *
 */
public class ItemOnCharacterDTO implements IsSerializable {

    private long ID;

    private int itemID;

    private ItemDTO assocItem;

    private long ownerID;

    private long count;

    private ItemLocation location;

    private int enchantLevel;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public ItemDTO getAssocItem() {
        return assocItem;
    }

    public void setAssocItem(ItemDTO assocItem) {
        this.assocItem = assocItem;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public long getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ItemLocation getLocation() {
        return location;
    }

    public void setLocation(ItemLocation location) {
        this.location = location;
    }

    public int getEnchantLevel() {
        return enchantLevel;
    }

    public void setEnchantLevel(int enchantLevel) {
        this.enchantLevel = enchantLevel;
    }
}
