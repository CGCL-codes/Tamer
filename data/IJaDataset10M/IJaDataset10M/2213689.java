package com.walker.model;

public class TickerItem {

    private String tickerSymbol;

    private double lastTradePrice;

    private java.util.Date quoteTimestamp;

    private double change;

    private double openPrice;

    private double dayHigh;

    private double dayLow;

    private int dayVolume;

    private String marketCap;

    private double prevClose;

    private String changePercent;

    private String wk52Range;

    private double eps;

    private double peRatio;

    private String companyName;

    public TickerItem() {
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public double getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(double lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public java.util.Date getQuoteTimestamp() {
        return quoteTimestamp;
    }

    public void setQuoteTimestamp(java.util.Date quoteTimestamp) {
        this.quoteTimestamp = quoteTimestamp;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getDayHigh() {
        return dayHigh;
    }

    public void setDayHigh(double dayHigh) {
        this.dayHigh = dayHigh;
    }

    public double getDayLow() {
        return dayLow;
    }

    public void setDayLow(double dayLow) {
        this.dayLow = dayLow;
    }

    public int getDayVolume() {
        return dayVolume;
    }

    public void setDayVolume(int dayVolume) {
        this.dayVolume = dayVolume;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public double getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(double prevClose) {
        this.prevClose = prevClose;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public String getWk52Range() {
        return wk52Range;
    }

    public void setWk52Range(String wk52Range) {
        this.wk52Range = wk52Range;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public double getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(double peRatio) {
        this.peRatio = peRatio;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
