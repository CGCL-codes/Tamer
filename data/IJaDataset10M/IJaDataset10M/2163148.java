package evetrader;

/**
 * Stores data on a particular trade
 * @author esumner
 *
 */
public class TradeEntry implements Comparable {

    public double price;

    public double volume;

    public int type;

    public int range;

    public int order;

    public int volEntered;

    public int minVolume;

    public boolean isBid;

    public boolean isNpc;

    public String issued;

    public int duration;

    public int stationid;

    public int regionid;

    public int ssid;

    public int jumps;

    public int compareTo(Object o) {
        if (o instanceof TradeEntry) {
            TradeEntry compare = (TradeEntry) o;
            if (compare.price > price) {
                return -1;
            }
            return 1;
        }
        return 0;
    }
}
