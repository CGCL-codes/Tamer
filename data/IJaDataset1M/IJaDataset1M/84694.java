package org.middleheaven.quantity.money;

public class ISOCurrency extends Currency {

    private java.util.Currency iso;

    public ISOCurrency(java.util.Currency currency) {
        this.iso = currency;
    }

    public int getDefaultFractionDigits() {
        return iso.getDefaultFractionDigits();
    }

    public String getISOCode() {
        return iso.getCurrencyCode();
    }

    @Override
    public String symbol() {
        return iso.getCurrencyCode();
    }
}
