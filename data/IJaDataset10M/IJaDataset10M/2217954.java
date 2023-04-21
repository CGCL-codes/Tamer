package com.k_int.gen.RecordSyntax_opac;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;
import com.k_int.gen.Z39_50_APDU_1995.InternationalString_codec;
import java.lang.String;

public class OPACRecord_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static OPACRecord_codec me = null;

    private holdingsData_inline135_codec i_holdingsdata_inline135_codec = holdingsData_inline135_codec.getCodec();

    private EXTERNAL_codec i_external_codec = EXTERNAL_codec.getCodec();

    public static synchronized OPACRecord_codec getCodec() {
        if (me == null) {
            me = new OPACRecord_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        OPACRecord_type retval = (OPACRecord_type) type_instance;
        if (sm.sequenceBegin()) {
            if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
                retval = new OPACRecord_type();
            }
            retval.bibliographicRecord = (EXTERNAL_type) sm.implicit_tag(i_external_codec, retval.bibliographicRecord, 128, 1, true, "bibliographicRecord");
            retval.holdingsData = (java.util.Vector) sm.implicit_tag(i_holdingsdata_inline135_codec, retval.holdingsData, 128, 2, true, "holdingsData");
            sm.sequenceEnd();
        }
        return retval;
    }
}
