package com.bluebrim.stroke.shared;

import com.bluebrim.base.shared.*;

/**
 * RMI-enabling interface for class com.bluebrim.stroke.impl.shared.CoStrokeLayer.
 * 
 * @author: Dennis Malmstr�m
 */
public interface CoStrokeLayerIF extends CoObjectIF, java.rmi.Remote, Cloneable, com.bluebrim.xml.shared.CoXmlEnabledIF {

    public CoStrokeLayerIF deepClone();

    CoDashColorIF getColor();

    CoDashIF getDash();

    float getWidthProportion();

    CoAbsoluteDashColorIF setAbsoluteColor();

    void setColor(CoDashColorIF s);

    void setOwner(CoMarkDirtyListener owner);

    void setWidthProportion(float p);
}
