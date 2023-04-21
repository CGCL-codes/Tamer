package core;

/**
 *
 * @author hp
 */
public class Equip extends GameObject implements Item {

    protected int EquipId;

    protected int buyingPrice = 0;

    protected int sellingPrice = 0;

    protected int ItemId;

    protected void finalize() {
    }

    public Equip(int newEquipId) {
        setObjectType(8);
        EquipId = newEquipId;
        if (newEquipId == 1) {
            setBuyingPrice(20);
            setSellingPrice(10);
            ItemId = 25;
        } else if (newEquipId == 2) {
            setBuyingPrice(20);
            setSellingPrice(10);
            ItemId = 26;
        }
    }

    public int getEquipId() {
        return EquipId;
    }

    public void setBuyingPrice(int newBuyingPrice) {
        buyingPrice = newBuyingPrice;
    }

    public void setSellingPrice(int newSellingPrice) {
        sellingPrice = newSellingPrice;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public int getItemId() {
        return ItemId;
    }

    public String getItemName() {
        switch(EquipId) {
            case 1:
                return "iron shell";
            case 2:
                return "binocullar";
        }
        return "";
    }

    public String getImagePath() {
        switch(EquipId) {
            case 1:
                return "/GUI/images/icons/eq_bullet.png";
            case 2:
                return "/GUI/images/icons/eq_binocullar.png";
        }
        return "";
    }
}
