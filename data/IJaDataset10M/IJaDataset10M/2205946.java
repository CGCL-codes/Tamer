package pokeman;

import java.io.Serializable;

/**
 * Defines an interface that all items abide by. An item
 * has an effect on another object, of type T. For example, a
 * potion will heal a pokemon, making it implement Item for
 * Pokemon. Other items will implement for Move and for 
 * 
 * @author Kunal
 */
public abstract class Item<T> implements Serializable {

    private int price;

    private String name;

    private int quantity;

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    /**
     * uses the item on the specified object. It returns true if it worked,
     * false if it didn't for whatever reason. If the item has only one use,
     * this method should reduce the quantity.
     * @param other
     * @return -1 if it failed otherwise, use as specified
     */
    public abstract int use(T other);

    /**
     * gets the price of the current item. The selling price will be half
     * of the price returned here.
     * @return the buying price of the current item
     */
    public int getPrice() {
        return price;
    }

    /**
     * gets the name of the current item. For example, a super potion
     * will return "Super Potion".
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * returns the current quantity of the item.
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * 
     * @param extra
     * @return false if the new resulting quantity is 
     */
    public boolean stockUp(int extra) {
        int tempstock = quantity + extra;
        if (tempstock > 99) {
            return false;
        } else {
            quantity = tempstock;
            return true;
        }
    }

    /**
     * this method is used to tell if the user already has one of this type.
     * if the user does, then methods that use this should increment, rather
     * than add a new item to the bag.
     * @param other
     * @return
     */
    public boolean equals(Object other) {
        return ((Item) other).getName().equals(name);
    }

    /**
     * protected so that only the subclass can change its price
     * @param price
     */
    protected void setPrice(int price) {
        this.price = price;
    }
}
