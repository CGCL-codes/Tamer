package com.bluebrim.layout.impl.server.decorator;

import java.awt.*;
import java.awt.geom.*;
import org.w3c.dom.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.layout.impl.shared.*;

/**
 * Abstract superclass for fill styles
 * 
 * @author: Dennis Malmstr�m
 */
public abstract class CoFillStyle extends CoObject implements CoFillStyleIF, com.bluebrim.xml.shared.CoXmlEnabledIF {

    public void xmlAddSubModel(String name, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) {
    }

    protected class MutableProxy implements CoRemoteFillStyleIF {

        public CoFillStyleIF.Owner m_owner;

        public void removePropertyChangeListener(CoPropertyChangeListener l) {
            CoFillStyle.this.removePropertyChangeListener(l);
        }

        public void addPropertyChangeListener(CoPropertyChangeListener l) {
            CoFillStyle.this.addPropertyChangeListener(l);
        }

        public CoFillStyleIF deepClone() {
            return CoFillStyle.this.deepClone();
        }

        public String getFactoryKey() {
            return CoFillStyle.this.getFactoryKey();
        }

        public Paint getPaint(Rectangle2D bounds) {
            return CoFillStyle.this.getPaint(bounds);
        }
    }

    protected MutableProxy createMutableProxy() {
        return new MutableProxy();
    }

    public CoFillStyleIF getMutableProxy(CoFillStyleIF.Owner owner) {
        MutableProxy mp = createMutableProxy();
        mp.m_owner = owner;
        return mp;
    }

    public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) {
    }

    public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
    }
}
