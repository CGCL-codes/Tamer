package org.eclipsetrader.repository.local.internal.types;

import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.eclipsetrader.core.feed.IOHLC;
import org.eclipsetrader.core.feed.OHLC;

@XmlRootElement(name = "bar")
public class OHLCType {

    @XmlAttribute(name = "date")
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Date date;

    @XmlAttribute(name = "open")
    private Double open;

    @XmlAttribute(name = "high")
    private Double high;

    @XmlAttribute(name = "low")
    private Double low;

    @XmlAttribute(name = "close")
    private Double close;

    @XmlAttribute(name = "volume")
    private Long volume;

    public OHLCType() {
    }

    public OHLCType(IOHLC ohlc) {
        this.date = ohlc.getDate();
        this.open = ohlc.getOpen();
        this.high = ohlc.getHigh();
        this.low = ohlc.getLow();
        this.close = ohlc.getClose();
        this.volume = ohlc.getVolume();
    }

    public IOHLC getOHLC() {
        return new OHLC(date, open, high, low, close, volume);
    }
}
