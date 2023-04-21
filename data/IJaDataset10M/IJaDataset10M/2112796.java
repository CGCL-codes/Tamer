package com.k_int.gen.AccessControlFormat_prompt_1;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;
import com.k_int.gen.Z39_50_APDU_1995.InternationalString_codec;
import java.lang.String;
import com.k_int.gen.Z39_50_APDU_1995.DiagRec_codec;
import com.k_int.gen.Z39_50_APDU_1995.DiagRec_type;

public class Response_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static Response_codec me = null;

    private ResponseItem152_codec i_responseitem152_codec = ResponseItem152_codec.getCodec();

    public static synchronized Response_codec getCodec() {
        if (me == null) {
            me = new Response_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        java.util.Vector retval = (java.util.Vector) type_instance;
        if (sm.sequenceBegin()) {
            if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
                retval = new java.util.Vector();
            }
            retval = sm.sequenceOf(retval, i_responseitem152_codec);
            sm.sequenceEnd();
        }
        return retval;
    }
}
