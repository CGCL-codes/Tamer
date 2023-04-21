package org.progeeks.mapview.wms;

import java.util.*;
import org.w3c.dom.*;
import org.progeeks.mapview.GeoPoint;

/**
 *
 *  @version   $Revision: 3938 $
 *  @author    Paul Speed
 */
public class GmlCoordinates extends ArrayList<GeoPoint> {

    public GmlCoordinates(Element gml) {
        String cs = XPathUtils.getAttributeString(gml, "cs");
        if (cs == null) cs = ",";
        String decimal = XPathUtils.getAttributeString(gml, "decimal");
        if (decimal == null) decimal = ".";
        String coords = gml.getTextContent();
        if (coords == null) return;
        String[] array = coords.split(" ");
        for (String s : array) {
            if (!" ".equals(cs)) s = s.replaceAll(cs, " ");
            if (!".".equals(decimal)) s = s.replaceAll(decimal, ".");
            String[] parts = s.split(" ");
            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            add(new GeoPoint(x, y));
        }
    }
}
