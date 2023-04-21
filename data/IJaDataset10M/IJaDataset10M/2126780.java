package org.sqsh.variables;

import org.sqsh.CannotSetValueError;
import org.sqsh.Variable;

/**
 * Represents a font specification.
 */
public class FontVariable extends Variable {

    private String fontName = "Monospaced";

    private int size = 10;

    @Override
    public String setValue(String value) throws CannotSetValueError {
        String[] parts = value.split("-");
        boolean ok = true;
        if (parts.length != 2) {
            ok = false;
        } else {
            try {
                int tmpSize = Integer.parseInt(parts[1]);
                size = tmpSize;
                fontName = parts[0];
            } catch (Exception e) {
                ok = false;
            }
        }
        if (!ok) {
            throw new CannotSetValueError("Invalid font specification '" + value + "'. Fonts must be specified as Name-Size, such " + " as 'Monospace-10'");
        }
        return null;
    }

    @Override
    public String toString() {
        return fontName + "-" + size;
    }

    public String getFontName() {
        return fontName;
    }

    public int getFontSize() {
        return size;
    }
}
