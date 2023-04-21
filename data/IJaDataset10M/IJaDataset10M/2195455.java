package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration _styleListStylePosition.
 */
public class _styleListStylePosition extends ComEnumeration {

    public static final int styleListStylePositionNotSet = 0;

    public static final int styleListStylePositionInside = 1;

    public static final int styleListStylePositionOutSide = 2;

    public static final int styleListStylePosition_Max = 0x7fffffff;

    public _styleListStylePosition() {
    }

    public _styleListStylePosition(long val) {
        super(val);
    }

    public _styleListStylePosition(IntegerParameter t) {
        super(t);
    }

    public Object clone() {
        return new _styleListStylePosition(this);
    }
}
