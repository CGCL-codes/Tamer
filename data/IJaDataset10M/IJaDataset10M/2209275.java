package org.xmlvm.ios;

import java.util.*;
import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public class MKPolygonView extends MKOverlayPathView {

    /**
	 * - (id)initWithPolygon:(MKPolygon *)polygon;
	 */
    public MKPolygonView(MKPolygon polygon) {
    }

    /** Default constructor */
    MKPolygonView() {
    }

    /**
	 * @property(nonatomic, readonly) MKPolygon *polygon;
	 */
    public MKPolygon getPolygon() {
        throw new RuntimeException("Stub");
    }
}
