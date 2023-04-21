package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration _styleTextLineThroughStyle.
 */
public class _styleTextLineThroughStyle extends ComEnumeration {

    public static final int styleTextLineThroughStyleUndefined = 0;

    public static final int styleTextLineThroughStyleSingle = 1;

    public static final int styleTextLineThroughStyleDouble = 2;

    public static final int styleTextLineThroughStyle_Max = 0x7fffffff;

    public _styleTextLineThroughStyle() {
    }

    public _styleTextLineThroughStyle(long val) {
        super(val);
    }

    public _styleTextLineThroughStyle(IntegerParameter t) {
        super(t);
    }

    public Object clone() {
        return new _styleTextLineThroughStyle(this);
    }
}
