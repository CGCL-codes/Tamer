package uk.co.christhomson.sibyl.sample.objects;

import java.io.Serializable;
import java.util.Date;

public class InstrumentPriceKey implements Serializable {

    private static final long serialVersionUID = -3878397878224454476L;

    private String ticker = null;

    private Date date = null;

    private PriceSource source = null;

    public InstrumentPriceKey() {
    }

    public InstrumentPriceKey(String ticker, Date date, PriceSource source) {
        this.ticker = ticker;
        this.date = date;
        this.source = source;
    }

    public String getTicker() {
        return ticker;
    }

    public Date getDate() {
        return date;
    }

    public PriceSource getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "InstrumentPriceKey [date=" + date + ", ticker=" + ticker + ", source=" + source + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ticker.hashCode();
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        InstrumentPriceKey other = (InstrumentPriceKey) obj;
        if (date == null) {
            if (other.date != null) return false;
        } else if (!date.equals(other.date)) return false;
        if (!ticker.equals(other.ticker)) return false;
        if (source == null) {
            if (other.source != null) return false;
        } else if (!source.equals(other.source)) return false;
        return true;
    }
}
