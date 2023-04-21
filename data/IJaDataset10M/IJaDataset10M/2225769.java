package jgnash.engine.commodity;

import jgnash.util.DateUtils;
import jgnash.xml.XMLData;

/** Price/Volume history node for a security.  The high and low values are
 * can be null if the user does not want to maintain that level of detail.
 *<p>
 *$Id: SecurityHistoryNode.java 675 2008-06-17 01:36:01Z ccavanaugh $
 *
 * @author  Craig Cavanaugh
 */
public class SecurityHistoryNode extends CommodityHistoryNode {

    float high;

    float low;

    long volume = 0;

    /** public no-argument constructor for reflection */
    public SecurityHistoryNode() {
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public long getVolume() {
        return volume;
    }

    public Object marshal(XMLData xml) {
        if (xml.isReading() && xml.fileVersion < 1) {
            date = DateUtils.levelDate(xml.marshal("date", date));
        } else {
            date = xml.marshal("date", date);
        }
        price = xml.marshal("price", price);
        volume = xml.marshal("volume", volume);
        high = xml.marshal("high", high);
        low = xml.marshal("low", low);
        return this;
    }
}
