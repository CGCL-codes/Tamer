package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.types.*;

/**
 * Represents COM enumeration _styleBidi.
 */
public class _styleBidi extends ComEnumeration {

    public static final int styleBidiNotSet = 0;

    public static final int styleBidiNormal = 1;

    public static final int styleBidiEmbed = 2;

    public static final int styleBidiOverride = 3;

    public static final int styleBidiInherit = 4;

    public static final int styleBidi_Max = 0x7fffffff;

    public _styleBidi() {
    }

    public _styleBidi(long val) {
        super(val);
    }

    public _styleBidi(IntegerParameter t) {
        super(t);
    }

    public Object clone() {
        return new _styleBidi(this);
    }
}
