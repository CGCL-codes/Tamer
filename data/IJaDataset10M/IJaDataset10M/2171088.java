package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration _ELEMENT_CORNER.
 */
public class _ELEMENT_CORNER extends ComEnumeration {

    public static final int ELEMENT_CORNER_NONE = 0;

    public static final int ELEMENT_CORNER_TOP = 1;

    public static final int ELEMENT_CORNER_LEFT = 2;

    public static final int ELEMENT_CORNER_BOTTOM = 3;

    public static final int ELEMENT_CORNER_RIGHT = 4;

    public static final int ELEMENT_CORNER_TOPLEFT = 5;

    public static final int ELEMENT_CORNER_TOPRIGHT = 6;

    public static final int ELEMENT_CORNER_BOTTOMLEFT = 7;

    public static final int ELEMENT_CORNER_BOTTOMRIGHT = 8;

    public static final int ELEMENT_CORNER_Max = 0x7fffffff;

    public _ELEMENT_CORNER() {
    }

    public _ELEMENT_CORNER(long val) {
        super(val);
    }

    public _ELEMENT_CORNER(IntegerParameter t) {
        super(t);
    }

    public Object clone() {
        return new _ELEMENT_CORNER(this);
    }
}
