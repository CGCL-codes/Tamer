package com.k_int.gen.AccessControlFormat_prompt_1;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;
import com.k_int.gen.Z39_50_APDU_1995.InternationalString_codec;
import java.lang.String;
import com.k_int.gen.Z39_50_APDU_1995.DiagRec_codec;
import com.k_int.gen.Z39_50_APDU_1995.DiagRec_type;

public class enummeratedPrompt_inline154_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static enummeratedPrompt_inline154_codec me = null;

    private Integer_codec i_integer_codec = Integer_codec.getCodec();

    private InternationalString_codec i_internationalstring_codec = InternationalString_codec.getCodec();

    public static synchronized enummeratedPrompt_inline154_codec getCodec() {
        if (me == null) {
            me = new enummeratedPrompt_inline154_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        enummeratedPrompt_inline154_type retval = (enummeratedPrompt_inline154_type) type_instance;
        if (sm.sequenceBegin()) {
            if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
                retval = new enummeratedPrompt_inline154_type();
            }
            retval.type = (BigInteger) sm.implicit_tag(i_integer_codec, retval.type, 128, 1, false, "type");
            retval.suggestedString = (java.lang.String) sm.implicit_tag(i_internationalstring_codec, retval.suggestedString, 128, 2, true, "suggestedString");
            sm.sequenceEnd();
        }
        return retval;
    }
}
