package de.purchasemgr.data.type;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import de.purchasemgr.i18n.Messages;

/**
 * This is the representation for a purchase, the clone from the reality.
 * 
 * @author croesch
 * @since Date: 2010/12/19 00:03:13
 */
public class Purchase {

    /** the date when the purchase took place */
    private final Date date;

    /** the shop in which the purchase took place */
    private final Shop shop;

    /** the list of items that where bought with this purchase */
    private final List<Item> items = new ArrayList<Item>();

    /**
   * Constructs a new purchase with the given parameters
   * 
   * @author croesch
   * @since Date: 23.01.2011 15:13:18
   * @param timeStamp the milliseconds of the date when the purchase happened
   * @param location the shop in which the purchase happened
   */
    public Purchase(final long timeStamp, final Shop location) {
        this.date = new Date(timeStamp);
        this.shop = location;
    }

    /**
   * Adds an item to this purchase
   * 
   * @author croesch
   * @since Date: 08.02.2011 18:19:10
   * @param item the item to add
   */
    final void addItem(final Item item) {
        this.items.add(item);
    }

    /**
   * Returns the location where the purchase took place
   * 
   * @author croesch
   * @since Date: 08.02.2011 18:19:57
   * @return the {@link Shop} in which the purchase took place
   */
    final Shop getShop() {
        return new Shop(this.shop);
    }

    /**
   * Returns the date, when the purchase took place
   * 
   * @author croesch
   * @since Date: 08.02.2011 18:23:07
   * @return the {@link Date} that represents the time, when the purchase took place
   */
    final Date getDate() {
        return this.date;
    }

    @Override
    public final String toString() {
        DateFormat sdf = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return Messages.PURCHASE_STRING.text(sdf.format(this.date), this.shop.toString());
    }
}
