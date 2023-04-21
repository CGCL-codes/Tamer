package com.k_int.gen.ESFormat_PersistentResultSet;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;
import com.k_int.gen.Z39_50_APDU_1995.InternationalString_codec;
import java.lang.String;

public class esRequest_inline155_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static esRequest_inline155_codec me = null;

    private OriginPartNotToKeep_codec i_originpartnottokeep_codec = OriginPartNotToKeep_codec.getCodec();

    private NULL_codec i_null_codec = NULL_codec.getCodec();

    public static synchronized esRequest_inline155_codec getCodec() {
        if (me == null) {
            me = new esRequest_inline155_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        esRequest_inline155_type retval = (esRequest_inline155_type) type_instance;
        if (sm.sequenceBegin()) {
            if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
                retval = new esRequest_inline155_type();
            }
            retval.toKeep = (com.k_int.codec.runtime.AsnNull) sm.implicit_tag(i_null_codec, retval.toKeep, 128, 1, false, "toKeep");
            retval.notToKeep = (OriginPartNotToKeep_type) sm.explicit_tag(i_originpartnottokeep_codec, retval.notToKeep, 128, 2, true, "notToKeep");
            sm.sequenceEnd();
        }
        return retval;
    }
}
