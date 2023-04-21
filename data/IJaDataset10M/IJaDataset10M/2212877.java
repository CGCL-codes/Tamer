package com.fh.auge.core.internal.watchlist;

import java.util.Map;
import com.domainlanguage.money.Money;
import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.Duration;
import com.fh.auge.core.IMarket;
import com.fh.auge.core.indices.Gain;
import com.fh.auge.core.security.ISecurity;
import com.fh.auge.core.security.Quote;

public class Investment {

    private ISecurity security;

    private IMarket market;

    private String comment;

    private CalendarDate purchaseDate;

    private double sharesOwned;

    private Money investedCapital;

    private Money value;

    private Gain investmentGain;

    private Map<Duration, Gain> gains;

    private Gain monthlyReturn;

    private Gain monthlyRisk;

    private Gain anualReturn;

    private Gain anualRisk;

    private Quote lastQuote;

    private Quote prevQuote;

    public String getName() {
        return security.getName();
    }

    public ISecurity getSecurity() {
        return security;
    }

    public void setSecurity(ISecurity security) {
        this.security = security;
    }

    public IMarket getMarket() {
        return market;
    }

    public void setMarket(IMarket market) {
        this.market = market;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CalendarDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(CalendarDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getSharesOwned() {
        return sharesOwned;
    }

    public void setSharesOwned(double sharesOwned) {
        this.sharesOwned = sharesOwned;
    }

    public Money getInvestedCapital() {
        return investedCapital;
    }

    public void setInvestedCapital(Money investedCapital) {
        this.investedCapital = investedCapital;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    public Gain getInvestmentGain() {
        return investmentGain;
    }

    public void setInvestmentGain(Gain investmentGain) {
        this.investmentGain = investmentGain;
    }

    public Map<Duration, Gain> getGains() {
        return gains;
    }

    public void setGains(Map<Duration, Gain> gains) {
        this.gains = gains;
    }

    public Gain getMonthlyReturn() {
        return monthlyReturn;
    }

    public void setMonthlyReturn(Gain monthlyReturn) {
        this.monthlyReturn = monthlyReturn;
    }

    public Gain getMonthlyRisk() {
        return monthlyRisk;
    }

    public void setMonthlyRisk(Gain monthlyRisk) {
        this.monthlyRisk = monthlyRisk;
    }

    public Gain getAnualReturn() {
        return anualReturn;
    }

    public void setAnualReturn(Gain anualReturn) {
        this.anualReturn = anualReturn;
    }

    public Gain getAnualRisk() {
        return anualRisk;
    }

    public void setAnualRisk(Gain anualRisk) {
        this.anualRisk = anualRisk;
    }

    public Quote getLastQuote() {
        return lastQuote;
    }

    public void setLastQuote(Quote lastQuote) {
        this.lastQuote = lastQuote;
    }

    public Quote getPrevQuote() {
        return prevQuote;
    }

    public void setPrevQuote(Quote prevQuote) {
        this.prevQuote = prevQuote;
    }
}
