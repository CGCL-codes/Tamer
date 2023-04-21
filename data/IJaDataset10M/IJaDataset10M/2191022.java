package com.k_int.gen.Z39_50_APDU_1995;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;

public class diagnostics_inline36_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static diagnostics_inline36_codec me = null;

    private DiagRec_codec i_diagrec_codec = DiagRec_codec.getCodec();

    public static synchronized diagnostics_inline36_codec getCodec() {
        if (me == null) {
            me = new diagnostics_inline36_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        java.util.Vector retval = (java.util.Vector) type_instance;
        if (sm.sequenceBegin()) {
            if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
                retval = new java.util.Vector();
            }
            retval = sm.sequenceOf(retval, i_diagrec_codec);
            sm.sequenceEnd();
        }
        return retval;
    }
}
