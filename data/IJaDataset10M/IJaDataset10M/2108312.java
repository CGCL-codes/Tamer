package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration _styleTextUnderlinePosition.
 */
public class _styleTextUnderlinePosition extends ComEnumeration {

    public static final int styleTextUnderlinePositionBelow = 0;

    public static final int styleTextUnderlinePositionAbove = 1;

    public static final int styleTextUnderlinePositionAuto = 2;

    public static final int styleTextUnderlinePositionNotSet = 3;

    public static final int styleTextUnderlinePosition_Max = 0x7fffffff;

    public _styleTextUnderlinePosition() {
    }

    public _styleTextUnderlinePosition(long val) {
        super(val);
    }

    public _styleTextUnderlinePosition(IntegerParameter t) {
        super(t);
    }

    public Object clone() {
        return new _styleTextUnderlinePosition(this);
    }
}
