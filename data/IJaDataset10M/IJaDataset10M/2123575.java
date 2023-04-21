package org.datanucleus.store.types;

import java.awt.geom.CubicCurve2D;
import java.util.StringTokenizer;
import org.datanucleus.exceptions.NucleusDataStoreException;

/**
 * Class to handle the conversion between java.awt.geom.CubicCurve2D.Float and a String form.
 * The String form is <pre>(x1,y1),(x2,y2),(xc1,yc1),(xc2,yc2)</pre>
 */
public class CubicCurve2dFloatStringConverter implements ObjectStringConverter<CubicCurve2D.Float> {

    public CubicCurve2D.Float toObject(String str) {
        if (str == null) {
            return null;
        }
        CubicCurve2D.Float cc = new CubicCurve2D.Float();
        StringTokenizer tokeniser = new StringTokenizer(str, ")");
        String token = tokeniser.nextToken();
        token = token.substring(1);
        String xStr = token.substring(0, token.indexOf(","));
        String yStr = token.substring(token.indexOf(",") + 1);
        float x1 = 0;
        try {
            x1 = Float.valueOf(xStr);
        } catch (NumberFormatException nfe) {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        float y1 = 0;
        try {
            y1 = Float.valueOf(yStr);
        } catch (NumberFormatException nfe) {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        token = tokeniser.nextToken();
        token = token.substring(2);
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",") + 1);
        float x2 = 0;
        try {
            x2 = Float.valueOf(xStr);
        } catch (NumberFormatException nfe) {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        float y2 = 0;
        try {
            y2 = Float.valueOf(yStr);
        } catch (NumberFormatException nfe) {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        token = tokeniser.nextToken();
        token = token.substring(2);
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",") + 1);
        float xc1 = 0;
        try {
            xc1 = Float.valueOf(xStr);
        } catch (NumberFormatException nfe) {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        float yc1 = 0;
        try {
            yc1 = Float.valueOf(yStr);
        } catch (NumberFormatException nfe) {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        token = tokeniser.nextToken();
        token = token.substring(2);
        xStr = token.substring(0, token.indexOf(","));
        yStr = token.substring(token.indexOf(",") + 1);
        float xc2 = 0;
        try {
            xc2 = Float.valueOf(xStr);
        } catch (NumberFormatException nfe) {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        float yc2 = 0;
        try {
            yc2 = Float.valueOf(yStr);
        } catch (NumberFormatException nfe) {
            throw new NucleusDataStoreException(LOCALISER.msg("016002", str, CubicCurve2D.Float.class.getName()), nfe);
        }
        cc.setCurve(x1, y1, x2, y2, xc1, yc1, xc2, yc2);
        return cc;
    }

    public String toString(CubicCurve2D.Float cc) {
        if (cc == null) {
            return null;
        }
        StringBuffer str = new StringBuffer();
        str.append("(").append(cc.x1).append(",").append(cc.y1).append("),");
        str.append("(").append(cc.x2).append(",").append(cc.y2).append("),");
        str.append("(").append(cc.ctrlx1).append(",").append(cc.ctrly1).append("),");
        str.append("(").append(cc.ctrlx2).append(",").append(cc.ctrly2).append(")");
        return str.toString();
    }
}
