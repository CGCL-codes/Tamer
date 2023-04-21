package com.javector.soashopper;

import java.util.Currency;
import com.javector.util.Util;

public class Price {

    private double value;

    private Currency currencyID;

    private Util util = new Util();

    /**
   * @param cur the currency using 3 letters as defined by {@link java.util.Currency}
   * @param val the price.
   * @return null if val is null.  Otherwise, the corresponding Price instance.
   * @throws IllegalArgumentException if the currency is null or invalid.
   */
    public static Price getPrice(String cur, Double val) {
        if (cur == null || Currency.getInstance(cur) == null) {
            throw new IllegalArgumentException("Currency: " + cur + " is invalid.");
        }
        if (val == null) {
            return null;
        }
        return new Price(Currency.getInstance(cur), val.doubleValue());
    }

    public Price(Currency currencyID, double value) {
        this.value = value;
        this.currencyID = currencyID;
    }

    public Currency getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Currency currencyID) {
        this.currencyID = currencyID;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String toString() {
        return util.toString(this);
    }

    /**
   * Greater than or equal to.
   * 
   * @param p the price to compare to
   * @return true iff this is greater than or equal to p and has the same currency
   */
    public boolean ge(Price p) {
        if (p == null) {
            return true;
        }
        return currencyID.getCurrencyCode().equals(p.getCurrencyID().getCurrencyCode()) && value >= p.getValue();
    }

    /**
   * Less than or equal to.
   * 
   * @param p the price to compare to
   * @return true iff this is less than or equal to p and has the same currency
   */
    public boolean le(Price p) {
        if (p == null) {
            return true;
        }
        return currencyID.getCurrencyCode().equals(p.getCurrencyID().getCurrencyCode()) && value <= p.getValue();
    }
}
