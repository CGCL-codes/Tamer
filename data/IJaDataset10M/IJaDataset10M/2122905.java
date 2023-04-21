package de.cue4net.eventservice.model.shared;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Keino Uelze - cue4net
 * @version $Id: MonetaryAmount.java,v 1.5 2008-06-05 12:19:10 keino Exp $
 */
public class MonetaryAmount {

    private BigDecimal value;

    private Currency currency;

    private static final Map<String, BigDecimal> exchangeRatesToEUR = new HashMap<String, BigDecimal>();

    static {
        exchangeRatesToEUR.put("EUR", new BigDecimal(1.0D));
    }

    public MonetaryAmount() {
        this.currency = Currency.getInstance("EUR");
        this.value = new BigDecimal(0.0D);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Creates a new Monetary Amount object for the result keeps the currency of THIS
     * @param otherMonetaryAmount
     * @return a new MonetaryAmount object which is the sum of this object and the parameter passed
     */
    public MonetaryAmount add(MonetaryAmount otherMonetaryAmount) {
        MonetaryAmount monetaryAmount = new MonetaryAmount();
        BigDecimal helperBD1 = new BigDecimal(0.0D);
        BigDecimal helperBD2 = new BigDecimal(0.0D);
        helperBD1 = this.value.multiply(exchangeRatesToEUR.get(this.currency.toString()));
        helperBD2 = otherMonetaryAmount.getValue().multiply(exchangeRatesToEUR.get(otherMonetaryAmount.getCurrency().toString()));
        helperBD1 = helperBD1.add(helperBD2);
        helperBD1 = helperBD1.divide(exchangeRatesToEUR.get(this.currency.toString()));
        monetaryAmount.setValue(helperBD1);
        monetaryAmount.setCurrency(Currency.getInstance(this.currency.toString()));
        return monetaryAmount;
    }

    /**
     * adds the otherMonetaryAmount to this and keeps the currency of THIS
     * @param otherMonetaryAmount
     */
    public void addToThis(MonetaryAmount otherMonetaryAmount) {
        BigDecimal helperBD1 = new BigDecimal(0.0D);
        BigDecimal helperBD2 = new BigDecimal(0.0D);
        helperBD1 = this.value.multiply(MonetaryAmount.exchangeRatesToEUR.get(this.currency.toString()));
        helperBD2 = otherMonetaryAmount.getValue().multiply(exchangeRatesToEUR.get(otherMonetaryAmount.getCurrency().toString()));
        helperBD1 = helperBD1.add(helperBD2);
        helperBD1 = helperBD1.divide(exchangeRatesToEUR.get(this.currency.toString()));
        this.setValue(helperBD1);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currency == null) ? 0 : currency.getCurrencyCode().hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof MonetaryAmount)) return false;
        final MonetaryAmount other = (MonetaryAmount) obj;
        if (currency == null) {
            if (other.getCurrency() != null) return false;
        } else if (!currency.getCurrencyCode().equals(other.getCurrency().getCurrencyCode())) return false;
        if (value == null) {
            if (other.getValue() != null) return false;
        } else if (!value.equals(other.getValue())) return false;
        return true;
    }
}
