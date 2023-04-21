package aml.ramava.data.entities;

import aml.ramava.common.Utilities;

public class VATRate {

    public static final VATRate VAT_FIVE = new VATRate(0.05d);

    public static final VATRate VAT_NINE = new VATRate(0.09d);

    public static final VATRate VAT_TEN = new VATRate(0.1d);

    public static final VATRate VAT_EIGHTTEEN = new VATRate(0.18d);

    public static final VATRate VAT_TWENTYONE = new VATRate(0.21d);

    public VATRate(double rate) {
        setRate(rate);
    }

    double rate;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String toString() {
        return Utilities.formatVATRate(getRate());
    }

    public boolean equals(Object o) {
        if (o instanceof VATRate) return this.rate == ((VATRate) o).getRate();
        return false;
    }
}
