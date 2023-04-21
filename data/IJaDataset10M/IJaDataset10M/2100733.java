package blue.soundObject.ceciliaModule;

import java.io.Serializable;
import electric.xml.Element;

/**
 * @author steven yi
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CGraphPoint implements Serializable, Comparable {

    public float time = 0.0f;

    public float value = 0.0f;

    public String toString() {
        return "[CGraphPoint] Time: " + time + " Value: " + value;
    }

    public static CGraphPoint loadFromXML(Element data) {
        CGraphPoint cgp = new CGraphPoint();
        cgp.time = Float.parseFloat(data.getAttributeValue("time"));
        cgp.value = Float.parseFloat(data.getAttributeValue("value"));
        return cgp;
    }

    public Element saveAsXML() {
        Element retVal = new Element("cgraphPoint");
        retVal.setAttribute("time", Float.toString(time));
        retVal.setAttribute("value", Float.toString(value));
        return retVal;
    }

    public int compareTo(Object o) {
        CGraphPoint a = this;
        CGraphPoint b = (CGraphPoint) o;
        float val = a.time - b.time;
        if (val > 0.0f) {
            return 1;
        } else if (val < 0.0f) {
            return -1;
        } else {
            return 0;
        }
    }
}
