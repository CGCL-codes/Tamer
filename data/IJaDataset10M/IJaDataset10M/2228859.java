package ws4java6.data;

import java.util.Date;

/**
 * Hold the data for a stock for a certain date. The following data is captured: -
 * The opening value of the stock on this date - The closing value of the stock
 * on this date - The highest value of the stock on this date - The lowest value
 * of the stock on this date - The volume of the stock which was traded on this
 * date
 * 
 * 
 * @author Lars Vogel
 * @since 0.1
 */
public class StockData implements IStockData {

    Date date;

    double open = 0;

    double high = 0;

    double low = 0;

    double close = 0;

    long volume = 0;

    public StockData() {
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}
