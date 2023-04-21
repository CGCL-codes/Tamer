package blue.components.lines;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.lang.builder.EqualsBuilder;
import electric.xml.Element;

/**
 * @author Steven Yi
 */
public class LinePoint implements Serializable, Comparable {

    private float y;

    private float x;

    private transient Vector listeners = null;

    private transient ChangeEvent changeEvent = null;

    public static LinePoint loadFromXML(Element data) {
        LinePoint lp = new LinePoint();
        float x = Float.parseFloat(data.getAttributeValue("x"));
        float y = Float.parseFloat(data.getAttributeValue("y"));
        lp.setLocation(x, y);
        return lp;
    }

    public Element saveAsXML() {
        Element retVal = new Element("linePoint");
        retVal.setAttribute("x", Float.toString(getX()));
        retVal.setAttribute("y", Float.toString(getY()));
        return retVal;
    }

    public int compareTo(Object o) {
        LinePoint a = this;
        LinePoint b = (LinePoint) o;
        float val = a.getX() - b.getX();
        if (val > 0.0f) {
            return 1;
        } else if (val < 0.0f) {
            return -1;
        } else {
            return 0;
        }
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        if (changeEvent == null) {
            changeEvent = new ChangeEvent(this);
        }
        fireChangeEvent(changeEvent);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void addChangeListener(ChangeListener pcl) {
        if (listeners == null) {
            listeners = new Vector();
        }
        listeners.add(pcl);
    }

    public void removeChangeListener(ChangeListener pcl) {
        if (listeners != null) {
            listeners.remove(pcl);
        }
    }

    public void fireChangeEvent(ChangeEvent pce) {
        if (listeners != null) {
            for (Iterator iter = listeners.iterator(); iter.hasNext(); ) {
                ChangeListener pcl = (ChangeListener) iter.next();
                pcl.stateChanged(pce);
            }
        }
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
