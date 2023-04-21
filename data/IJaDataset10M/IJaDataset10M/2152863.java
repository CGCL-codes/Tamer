package org.datanucleus.store.types;

import java.awt.Polygon;
import java.util.StringTokenizer;
import org.datanucleus.exceptions.NucleusDataStoreException;

/**
 * Class to handle the conversion between java.awt.Polygon and a String form.
 * The String form is <pre>[(x1,y1),(x2,y2)[,(xn, yn)...])</pre>
 */
public class PolygonStringConverter implements ObjectStringConverter<Polygon> {

    public Polygon toObject(String str) {
        if (str == null) {
            return null;
        }
        Polygon p = new Polygon();
        if (str.length() <= 2) {
            return p;
        }
        String tmpStr = str.substring(1, str.length() - 1);
        StringTokenizer tokeniser = new StringTokenizer(tmpStr.substring(1, tmpStr.length() - 1), "(");
        if (tokeniser.hasMoreTokens()) {
            String token = tokeniser.nextToken().trim();
            token = token.substring(0, token.indexOf(")"));
            String xStr = token.substring(0, token.indexOf(","));
            String yStr = token.substring(token.indexOf(",") + 1);
            int x = 0;
            int y = 0;
            try {
                x = Integer.valueOf(xStr);
            } catch (NumberFormatException nfe) {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Polygon.class.getName()), nfe);
            }
            try {
                y = Integer.valueOf(yStr);
            } catch (NumberFormatException nfe) {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Polygon.class.getName()), nfe);
            }
            p.addPoint(x, y);
        } else {
            return null;
        }
        return p;
    }

    public String toString(Polygon poly) {
        if (poly == null) {
            return null;
        }
        StringBuffer str = new StringBuffer("[");
        for (int i = 0; i < poly.npoints; i++) {
            str.append("(").append(poly.xpoints[i]).append(",").append(poly.ypoints[i]).append(")");
            if (i < poly.npoints - 1) {
                str.append(",");
            }
        }
        str.append("]");
        return str.toString();
    }
}
