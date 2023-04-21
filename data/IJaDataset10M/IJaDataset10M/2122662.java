package com.ivis.xprocess.core;

import java.util.Iterator;
import java.util.Set;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.annotations.Property;
import com.ivis.xprocess.framework.properties.PropertyType;
import com.ivis.xprocess.util.Day;
import com.ivis.xprocess.util.Money;

/**
 * Pricing object handles the prices for a PricedItem.
 *
 * Is contained in the PricedItem, using the framework to get a reference to it
 * if it needs to.
 * <p>
 * The pricing of a WorkPackage is used to define completion <b>value</b>. <br>
 * The pricing of a Party is used to define the <b>cost</b> of using a Project
 * Resource. It is assumed to be per hour.
 *
 */
@com.ivis.xprocess.framework.annotations.Element(designator = "PC")
public interface Pricing extends Xelement {

    /**
     * Property name for Price Records
     */
    public static final String PRICE_RECORDS = "PRICE_RECORDS";

    /**
     * Sets the price in the specified date range
     *
     * @param priceRecord
     */
    public void addPriceRecord(PriceRecord priceRecord);

    /**
     * Set the price between the two dates.
     *
     * @param from -
     *            start date for price
     * @param to -
     *            end date for price
     * @param money -
     *            the price
     */
    public void setPriceRecordWithPrice(Day from, Day to, Money money);

    /**
     * Set the price to be whatever price is defined on
     * <code>defaultPricing</code>
     *
     * @param from
     *            start date for price
     * @param to
     *            end date for price
     * @param defaultPricing -
     *            where pricing comes from.
     */
    public void setPriceRecordWithDefault(Day from, Day to, PricedItem defaultPricing);

    /**
     * @param day
     * @return The actual price record applicable to the specified day from a
     *         collection of PriceRecords held by the Priced Item or by a Priced
     *         Item in its default chain (returns <null>if price is unknown on
     *         that day)
     */
    PriceRecord getActualPriceRecordOn(Day day);

    /**
     * The records be derived from any of the pricing objects in the chain of
     * defaultPricing objects. The From and To dates of the records will not
     * overlap. That is the date of the From date of one record may not be
     * before the To date of the previous record even if the record it is
     * derived from (from a default object in the default chain) has a To date
     * that is later. If there is a gap between the To date of one record and
     * the From date of the next this indicates that the price is undefined in
     * the intervening period.
     *
     *
     * @param from
     *            The date from which the first record returned will start.
     * @param to
     *            The date to which the last record returned will apply.
     * @return a set of PriceRecords with details of known prices from one date
     *         to another.
     *
     */
    PriceRecord[] getPriceRecords(Day from, Day to);

    @Property(name = PRICE_RECORDS, propertyType = PropertyType.RECORDSET)
    public Set<PriceRecord> getPriceRecords();

    /**
     * @param record
     *            The actual record to be removed
     */
    void removePricingRecord(PriceRecord record);

    /**
     * @param pricing
     * @return true if the object <pricing>is in the default chain.
     */
    boolean hasInDefaultChain(PricedItem pricing);

    /**
     * @return a set of CSV records with details of prices over all dates from
     *         "from" to "to".
     *
     * Periods with no defined price are shown as UNKNOWN. From and To dates are
     * contiguous from beginning to end.
     */
    String getPriceDateDetailsCSV(Day from, Day to);

    /**
     * This method is used to find the set of PriceRecords applicable to a
     * priced item. The method is for internal use only; it should not be used
     * except by the Pricing Class. Instead use getPriceRecords().
     * @see #getPriceRecords()
     * @param day
     * @param followingRecords
     * @return - null (should not be used)
     */
    PriceRecord getCurrentAndFollowingRecordsOn(Day day, Iterator<PriceRecord> followingRecords);

    /**
     * @param to
     * @return the next record that is applicable on a date after the changeDay.
     *
     * This method is for internal use.
     */
    PriceRecord getNextLocalPriceAfter(Day to);

    /**
     * Removes any pricing records between the dates specified. This enables the
     * priced item to use prices from its default chain.
     *
     * @param from
     * @param to
     */
    void resetPricing(Day from, Day to);

    /**
     * Is the Price Record for this day derived from another PricedItem?
     *
     * @param day
     * @return true if derived, otherwise false
     */
    public boolean isPricingDerived(Day day);

    /**
     * @param day
     * @return - the Money on the PriceReord for that day, null if no record found
     */
    public Money getPrice(Day day);

    /**
     * @param from
     * @param to
     * @return an array of Money objects between the days passed in
     */
    public Money[] getPrice(Day from, Day to);
}
