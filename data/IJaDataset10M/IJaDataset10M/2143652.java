package org.jumpmind.pos.domain.currency;

import java.math.BigDecimal;

public class Money {

    private Currency currency;

    private BigDecimal amount = BigDecimal.ZERO;

    public static Money zero(Currency currency) {
        return new Money(currency);
    }

    public Money(Currency currency) {
        this.currency = currency;
    }

    public Money(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
