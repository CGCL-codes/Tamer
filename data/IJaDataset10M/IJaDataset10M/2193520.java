package net.cevn.galaxy;

import net.cevn.util.Identifiable;

/**
 * The <code>Item</code> interface represents anything that can be purchased or sold by a ship or player. This can be anything
 * from resources to weapons. 
 * 
 * @author Christopher Field <cfield2@gmail.com>
 * @version
 * @since 0.0.1
 */
public interface Item extends Identifiable {

    /**
	 * Gets the amount of space consumed by all units currently within a cargo hold.
	 * 
	 * @return The tonage, or amount of space consumed by this item within a cargo hold.
	 */
    public int getTonage();

    /**
	 * Gets the amount of space consumed by one unit of this item.
	 * 
	 * @return The unit tonage.
	 */
    public int getUnitTonage();

    /**
	 * Sets the amount of space consumed by all units of this item currently within a cargo hold.
	 * 
	 * @param tonage The amount of space consumed by this item within a cargo hold.
	 * @throws IllegalArgumentException If the tonage is less than zero.
	 */
    public void setTonage(final int tonage);

    /**
	 * Sets the unit tonage. This is the amount of space consumed by just one of this item.
	 * 
	 * @param unitTonage The unit tonage.
	 * @throws IllegalArgumentException If the unit tonage is less than zero.
	 */
    public void setUnitTonage(final int unitTonage);

    /**
	 * Gets the purchase price in credits. This is the sum of all units times the unit price.
	 * 
	 * @return The total purchase price in credits.
	 */
    public int getPrice();

    /**
	 * Gets the price for one unit.
	 * 
	 * @return The unit price.
	 */
    public int getUnitPrice();

    /**
	 * Sets the price in credits per one unit.
	 * 
	 * @param unitPrice The price per one unit.
	 */
    public void setUnitPrice(final int unitPrice);
}
