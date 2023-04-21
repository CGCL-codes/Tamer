package com.ivis.xprocess.core;

import java.util.Currency;
import com.ivis.xprocess.framework.Xrecord;
import com.ivis.xprocess.framework.annotations.Property;
import com.ivis.xprocess.framework.annotations.RecordKey;
import com.ivis.xprocess.framework.properties.PropertyType;
import com.ivis.xprocess.util.Day;
import com.ivis.xprocess.util.Money;

/**
 * Holds the price of an item between two dates
 *
 * Price can either be:
 *
 * Money (a currency and an amount) - this can be set to null,
 * which means "undefined price".
 *
 * Default Pricing - a PricedItem to get the price from.
 */
@com.ivis.xprocess.framework.annotations.Record(designator = "PRICE_RECORD")
public interface PriceRecord extends Xrecord {

    /**
     * Property name for From
     */
    @RecordKey(index = 0)
    public static final String FROM = "FROM";

    /**
     * Property name for To
     */
    @RecordKey(index = 1)
    public static final String TO = "TO";

    /**
     * Property name for Money
     */
    public static final String MONEY = "MONEY";

    /**
     * Property name for Default Pricing
     */
    public static final String DEFAULT_PRICING = "DEFAULT_PRICING";

    @Property(name = TO, propertyType = PropertyType.DAY)
    public Day getTo();

    @Property(name = FROM, propertyType = PropertyType.DAY)
    public Day getFrom();

    /**
     * @return the price of this price record. If null, means that price is
     *         "undefined"
     */
    @Property(name = MONEY, propertyType = PropertyType.MONEY)
    public Money getMoney();

    /**
     * @return a PricedItem that is to be used to get the price for this
     *         PriceRecord, or null if it does not have one
     */
    @Property(name = DEFAULT_PRICING, propertyType = PropertyType.REFERENCE)
    public PricedItem getDefaultPricing();

    /**
     * Set the PrcedItem that by default it should use to obtain its information.
     *
     * @param pricedItem
     */
    public void setDefaultPricing(PricedItem pricedItem);

    /**
     * Does the PriceRecord cover this day?
     *
     * @param day
     * @return true if the day falls into the records date range, otherwise false
     */
    boolean covers(Day day);

    /**
     * @param moneyValue
     */
    public void setMoneyValue(Money moneyValue);

    public void setCurrency(Currency moneyValue);

    public void setAmount(double moneyValue);
}
