package com.fh.auge.core;

import com.domainlanguage.money.Money;

public class InvestmentSnapshot extends AbstractSnapshot {

    Investment investment;

    Quote last;

    Money stopLoss;

    Money investedCapital;

    public Quote getLast() {
        return last;
    }

    public Money getStopLoss() {
        return stopLoss;
    }

    public Money getValue() {
        return value;
    }

    public Money getInvestedCapital() {
        return investedCapital;
    }

    public Investment getInvestment() {
        return investment;
    }
}
