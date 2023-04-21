package com.ivis.xprocess.core;

import com.ivis.xprocess.util.Day;
import com.ivis.xprocess.util.IMoney;
import com.ivis.xprocess.util.Money;

/**
 * ChargeRecord is not a persisted type. It is a marker interface
 *
 */
public interface ChargeRecord {

    /**
     * The type is dependent on the type of ProcedItem passed to setRoleOrConsumable()
     *
     * @return the type of ChargeRecord
     */
    public String getType();

    /**
     * @return the chargeable item associated with the ChargeRecord; Project Task etc...
     */
    public ChargeableItem getChargeableItem();

    /**
     * @return the name of the ChargeRecord, often the name of the ChargeableItem
     */
    public String getItemName();

    /**
     * @return day - when the records start from
     */
    public Day getFrom();

    /**
     * @return day - where the record go up to
     */
    public Day getTo();

    /**
     * @return the PricedItem
     */
    public PricedItem getRoleOrConsumable();

    /**
     * @return quantity
     */
    public double getQuantity();

    /**
     * @return price
     */
    public Money getPrice();

    /**
     * @return amount
     */
    public IMoney getAmount();

    /**
     * @param name
     */
    public void setItemName(String name);

    /**
     * @param from
     */
    public void setFrom(Day from);

    /**
     * @param to
     */
    public void setTo(Day to);

    /**
     * A PricedItem is a Consumable, Person or other.
     *
     * @param pricedItem
     */
    public void setRoleOrConsumable(PricedItem pricedItem);

    /**
     * @param quantity
     */
    public void setQuantity(double quantity);

    /**
     * @param price
     */
    public void setPrice(Money price);

    /**
     * @param money
     */
    public void setAmount(IMoney money);

    /**
     * @return a comma separated string representation of the ChargeRecord
     */
    public String getChargeRecordCSV();

    /**
     * Update the details of this ChargeRecord with the ChargeRecord passed in.
     * Used to merge records when they are the same.
     *
     * @param newChargeRecord
     */
    public void updateChargeRecord(ChargeRecord newChargeRecord);
}
