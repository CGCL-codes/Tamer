package com.fh.auge.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import com.domainlanguage.money.Money;

public class OHLC implements Serializable {

    protected Money open;

    protected Money hi;

    protected Money low;

    protected Money close;

    public enum Type {

        OPEN, HI, LOW, CLOSE
    }

    public OHLC(Money open, Money hi, Money low, Money close) {
        super();
        this.open = open;
        this.hi = hi;
        this.low = low;
        this.close = close;
    }

    public OHLC(double open, double hi, double low, double close, Currency currency) {
        super();
        this.open = Money.valueOf(open, currency);
        this.hi = Money.valueOf(hi, currency);
        this.low = Money.valueOf(low, currency);
        this.close = Money.valueOf(close, currency);
    }

    public Money getOpen() {
        return open;
    }

    public Money getHi() {
        return hi;
    }

    public Money getLow() {
        return low;
    }

    public Money getClose() {
        return close;
    }

    public Money get(Type type) {
        Money m = null;
        switch(type) {
            case OPEN:
                m = getOpen();
                break;
            case HI:
                m = getHi();
                break;
            case LOW:
                m = getLow();
                break;
            case CLOSE:
                m = getClose();
                break;
        }
        return m;
    }

    public OHLC plus(OHLC ohlc) {
        return new OHLC(getOpen().plus(ohlc.getOpen()), getHi().plus(ohlc.getHi()), getLow().plus(ohlc.getLow()), getClose().plus(ohlc.getClose()));
    }

    public OHLC convert(Currency currency, double rate) {
        BigDecimal convertRate = BigDecimal.valueOf(rate);
        return new OHLC(get(Type.OPEN).breachEncapsulationOfAmount().multiply(convertRate).doubleValue(), get(Type.HI).breachEncapsulationOfAmount().multiply(convertRate).doubleValue(), get(Type.LOW).breachEncapsulationOfAmount().multiply(convertRate).doubleValue(), get(Type.CLOSE).breachEncapsulationOfAmount().multiply(convertRate).doubleValue(), currency);
    }
}
