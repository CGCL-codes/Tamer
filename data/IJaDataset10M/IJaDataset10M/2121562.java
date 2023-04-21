package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration _styleDir.
 */
public class _styleDir extends ComEnumeration {

    public static final int styleDirNotSet = 0;

    public static final int styleDirLeftToRight = 1;

    public static final int styleDirRightToLeft = 2;

    public static final int styleDirInherit = 3;

    public static final int styleDir_Max = 0x7fffffff;

    public _styleDir() {
    }

    public _styleDir(long val) {
        super(val);
    }

    public _styleDir(IntegerParameter t) {
        super(t);
    }

    public Object clone() {
        return new _styleDir(this);
    }
}
