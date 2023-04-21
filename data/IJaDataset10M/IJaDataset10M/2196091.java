package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration _COORD_SYSTEM.
 */
public class _COORD_SYSTEM extends ComEnumeration {

    public static final int COORD_SYSTEM_GLOBAL = 0;

    public static final int COORD_SYSTEM_PARENT = 1;

    public static final int COORD_SYSTEM_CONTAINER = 2;

    public static final int COORD_SYSTEM_CONTENT = 3;

    public static final int COORD_SYSTEM_FRAME = 4;

    public static final int COORD_SYSTEM_Max = 0x7fffffff;

    public _COORD_SYSTEM() {
    }

    public _COORD_SYSTEM(long val) {
        super(val);
    }

    public _COORD_SYSTEM(IntegerParameter t) {
        super(t);
    }

    public Object clone() {
        return new _COORD_SYSTEM(this);
    }
}
