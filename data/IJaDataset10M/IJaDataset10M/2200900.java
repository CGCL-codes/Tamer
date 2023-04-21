package org.datanucleus.store.types;

import java.awt.geom.Rectangle2D;
import java.util.StringTokenizer;
import org.datanucleus.exceptions.NucleusDataStoreException;

/**
 * Class to handle the conversion between java.awt.geom.Rectangle2D.Float and a String form.
 * The String form is <pre>(x,y,width,height)</pre>
 */
public class Rectangle2dFloatStringConverter implements ObjectStringConverter<Rectangle2D.Float> {

    public Rectangle2D.Float toObject(String str) {
        if (str == null) {
            return null;
        }
        Rectangle2D.Float r = new Rectangle2D.Float();
        StringTokenizer tokeniser = new StringTokenizer(str.substring(1, str.length() - 1), ",");
        float x = 0;
        if (tokeniser.hasMoreTokens()) {
            String token = tokeniser.nextToken().trim();
            try {
                x = Float.valueOf(token);
            } catch (NumberFormatException nfe) {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Rectangle2D.Float.class.getName()), nfe);
            }
        } else {
            return null;
        }
        float y = 0;
        if (tokeniser.hasMoreTokens()) {
            String token = tokeniser.nextToken().trim();
            try {
                y = Float.valueOf(token);
            } catch (NumberFormatException nfe) {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Rectangle2D.Float.class.getName()), nfe);
            }
        } else {
            return null;
        }
        float width = 0;
        if (tokeniser.hasMoreTokens()) {
            String token = tokeniser.nextToken().trim();
            try {
                width = Float.valueOf(token);
            } catch (NumberFormatException nfe) {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Rectangle2D.Float.class.getName()), nfe);
            }
        } else {
            return null;
        }
        float height = 0;
        if (tokeniser.hasMoreTokens()) {
            String token = tokeniser.nextToken().trim();
            try {
                height = Float.valueOf(token);
            } catch (NumberFormatException nfe) {
                throw new NucleusDataStoreException(LOCALISER.msg("016002", str, Rectangle2D.Float.class.getName()), nfe);
            }
        } else {
            return null;
        }
        r.setRect(x, y, width, height);
        return r;
    }

    public String toString(Rectangle2D.Float rect) {
        return rect != null ? "(" + rect.getX() + "," + rect.getY() + "," + rect.getWidth() + "," + rect.getHeight() + ")" : null;
    }
}
