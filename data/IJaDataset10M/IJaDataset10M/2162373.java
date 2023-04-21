package org.apache.batik.bridge;

import org.apache.batik.gvt.Marker;
import org.w3c.dom.Element;

/**
 * Factory class for vending <tt>Marker</tt> objects.
 *
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id: MarkerBridge.java,v 1.1 2005/11/21 09:51:18 dev Exp $
 */
public interface MarkerBridge extends Bridge {

    /**
     * Creates a <tt>Marker</tt> according to the specified parameters.
     *
     * @param ctx the bridge context to use
     * @param markerElement the element that represents the marker
     * @param paintedElement the element that references the marker element
     */
    Marker createMarker(BridgeContext ctx, Element markerElement, Element paintedElement);
}
