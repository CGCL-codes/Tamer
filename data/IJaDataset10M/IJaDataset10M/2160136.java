package com.k_int.gen.RecordSyntax_generic;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;
import com.k_int.gen.Z39_50_APDU_1995.IntUnit_codec;
import com.k_int.gen.Z39_50_APDU_1995.IntUnit_type;
import com.k_int.gen.Z39_50_APDU_1995.Unit_codec;
import com.k_int.gen.Z39_50_APDU_1995.Unit_type;
import com.k_int.gen.Z39_50_APDU_1995.InternationalString_codec;
import java.lang.String;
import com.k_int.gen.Z39_50_APDU_1995.StringOrNumeric_codec;
import com.k_int.gen.Z39_50_APDU_1995.StringOrNumeric_type;
import com.k_int.gen.Z39_50_APDU_1995.Term_codec;
import com.k_int.gen.Z39_50_APDU_1995.Term_type;

public class TagPathItem142_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static TagPathItem142_codec me = null;

    private Integer_codec i_integer_codec = Integer_codec.getCodec();

    private StringOrNumeric_codec i_stringornumeric_codec = StringOrNumeric_codec.getCodec();

    public static synchronized TagPathItem142_codec getCodec() {
        if (me == null) {
            me = new TagPathItem142_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        TagPathItem142_type retval = (TagPathItem142_type) type_instance;
        if (sm.sequenceBegin()) {
            if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
                retval = new TagPathItem142_type();
            }
            retval.tagType = (BigInteger) sm.implicit_tag(i_integer_codec, retval.tagType, 128, 1, true, "tagType");
            retval.tagValue = (StringOrNumeric_type) sm.explicit_tag(i_stringornumeric_codec, retval.tagValue, 128, 2, false, "tagValue");
            retval.tagOccurrence = (BigInteger) sm.implicit_tag(i_integer_codec, retval.tagOccurrence, 128, 3, true, "tagOccurrence");
            sm.sequenceEnd();
        }
        return retval;
    }
}
