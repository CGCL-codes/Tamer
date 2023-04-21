package org.mov.quote;

import org.mov.ui.QuoteFormat;
import org.mov.util.TradingDate;
import org.mov.util.TradingTime;

/**
 * Representation of an intra-day stock quote for a given stock, on a given
 * day at a given time.
 *
 * @author Andrew Leppard
 */
public class IDQuote implements Quote {

    private Symbol symbol;

    private TradingDate date;

    private TradingTime time;

    private int currentVolume;

    private double currentLow;

    private double currentHigh;

    private double dayOpen;

    private double last;

    private double bid;

    private double ask;

    /**
     * Create a new intra-day stock quote.
     *
     * @param	symbol	       the stock symbol
     * @param	date	       the date of this stock quote
     * @param   time           the time of this stock quote
     * @param   currentVolume  the number of shares traded so far today
     * @param   currentLow     the current day low
     * @param   currentHigh    the current day high
     * @param	dayOpen        the opening quote on this date
     * @param   last           the last trade price
     * @param   bid            the last bid price
     * @param   ask            the last ask price
     */
    public IDQuote(Symbol symbol, TradingDate date, TradingTime time, int currentVolume, double currentLow, double currentHigh, double dayOpen, double last, double bid, double ask) {
        setSymbol(symbol);
        setDate(date);
        setTime(time);
        this.currentVolume = currentVolume;
        this.currentLow = currentLow;
        this.currentHigh = currentHigh;
        this.dayOpen = dayOpen;
        this.last = last;
        this.bid = bid;
        this.ask = ask;
    }

    /**
     * Clean up the quote. This fixes any irregularities that might exist in the quote,
     * such as the day low not being the lowest quote. This function does not
     * throw an exception, unlike {@link EODQuote#verify}. The reason is that the
     * intra-day quotes are downloaded all the time and it would only annoy the user
     * constanty see any warnig messages displayed.
     */
    public void verify() {
        if (currentLow > dayOpen || currentLow > last || currentLow > currentHigh) currentLow = Math.min(Math.min(dayOpen, last), currentHigh);
        if (currentHigh < dayOpen || currentHigh < last || currentHigh < currentLow) currentHigh = Math.max(Math.max(dayOpen, last), currentLow);
        if (currentLow < 0) currentLow = 0;
        if (currentHigh < 0) currentHigh = 0;
        if (dayOpen < 0) dayOpen = 0;
        if (last < 0) last = 0;
        if (bid < 0) bid = 0;
        if (ask < 0) ask = 0;
        if (currentVolume < 0) currentVolume = 0;
    }

    /**
     * Return the stock's symbol.
     *
     * @return	the symbol
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * Return the quote date.
     *
     * @return	the date
     */
    public TradingDate getDate() {
        return date;
    }

    /**
     * Return the quote time.
     *
     * @return	the time
     */
    public TradingTime getTime() {
        return time;
    }

    /**
     * Return the current day volume.
     *
     * @return	the current day volume
     */
    public int getDayVolume() {
        return currentVolume;
    }

    /**
     * Return the current day low.
     *
     * @return	the current day low
     */
    public double getDayLow() {
        return currentLow;
    }

    /**
     * Return the current day high.
     *
     * @return	the current day high
     */
    public double getDayHigh() {
        return currentHigh;
    }

    /**
     * Return the day open.
     *
     * @return	the day open
     */
    public double getDayOpen() {
        return dayOpen;
    }

    /**
     * Return the last trade value.
     *
     * @return	the last trade value.
     */
    public double getDayClose() {
        return last;
    }

    /**
     * Return the bid price.
     *
     * @return the bid price.
     */
    public double getBid() {
        return bid;
    }

    /**
     * Return the ask price.
     *
     * @return the ask price.
     */
    public double getAsk() {
        return ask;
    }

    /**
     * Set the symbol for this quote.
     *
     * @param	symbol	the stock symbol
     */
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    /**
     * Set the quote date.
     *
     * @param	date	the date
     */
    public void setDate(TradingDate date) {
        this.date = date;
    }

    /**
     * Set the quote time.
     *
     * @param	time	the time
     */
    public void setTime(TradingTime time) {
        this.time = time;
    }

    public double getQuote(int quote) throws UnsupportedOperationException {
        switch(quote) {
            case (DAY_OPEN):
                return getDayOpen();
            case (DAY_CLOSE):
                return getDayClose();
            case (DAY_LOW):
                return getDayLow();
            case (DAY_HIGH):
                return getDayHigh();
            case (DAY_VOLUME):
                return getDayVolume();
            case (BID):
                return getBid();
            case (ASK):
                return getAsk();
            default:
                throw new UnsupportedOperationException();
        }
    }

    /**
     * Return a string representation of the stock quote.
     *
     * @return	a string representation of the stock quote.
     */
    public String toString() {
        return new String(getSymbol() + ", " + getDate() + ", " + getTime() + ", " + QuoteFormat.quoteToString(getDayOpen()) + ", " + QuoteFormat.quoteToString(getDayHigh()) + ", " + QuoteFormat.quoteToString(getDayLow()) + ", " + QuoteFormat.quoteToString(getDayClose()) + ", " + getDayVolume() + ", " + QuoteFormat.quoteToString(getBid()) + ", " + QuoteFormat.quoteToString(getAsk()));
    }
}
