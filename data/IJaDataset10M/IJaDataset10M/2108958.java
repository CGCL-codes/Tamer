package com.fh.auge.performance;

import java.io.Serializable;
import java.math.BigDecimal;
import com.domainlanguage.base.Rounding;
import com.domainlanguage.money.Money;

public class Gain implements Serializable {

    private Money value;

    private BigDecimal percantage;

    public Gain(Money value, BigDecimal percantage) {
        super();
        this.value = value;
        this.percantage = percantage;
    }

    public Money getValue() {
        return value;
    }

    public BigDecimal getPercantage() {
        return percantage;
    }

    public static Gain create(Money invested, Money returned) {
        if (invested == null || invested.isZero() || returned == null || returned.isZero()) return null;
        Money m = returned.minus(invested);
        BigDecimal p = m.dividedBy(invested).decimalValue(4, Rounding.HALF_EVEN);
        return new Gain(m, p);
    }

    public boolean isNegative() {
        return value.isNegative();
    }

    public boolean isPositive() {
        return value.isPositive();
    }

    public boolean isZero() {
        return value.isZero();
    }

    public double getAmount() {
        return value.breachEncapsulationOfAmount().doubleValue();
    }

    public Gain divide(double divisor) {
        Money m = value.dividedBy(divisor, Rounding.HALF_EVEN);
        BigDecimal p = percantage.divide(BigDecimal.valueOf(divisor), BigDecimal.ROUND_HALF_EVEN);
        return new Gain(m, p);
    }

    public Gain times(int i) {
        Money m = value.times(i);
        BigDecimal p = percantage.multiply(BigDecimal.valueOf(i));
        return new Gain(m, p);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((percantage == null) ? 0 : percantage.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Gain other = (Gain) obj;
        if (percantage == null) {
            if (other.percantage != null) return false;
        } else if (!percantage.equals(other.percantage)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    @Override
    public String toString() {
        return value + " " + percantage;
    }
}
