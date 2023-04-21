package com.k_int.gen.RecordSyntax_explain;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;
import com.k_int.gen.Z39_50_APDU_1995.AttributeSetId_codec;
import com.k_int.gen.Z39_50_APDU_1995.Term_codec;
import com.k_int.gen.Z39_50_APDU_1995.Term_type;
import com.k_int.gen.Z39_50_APDU_1995.OtherInformation_codec;
import java.util.Vector;
import com.k_int.gen.Z39_50_APDU_1995.DatabaseName_codec;
import java.lang.String;
import com.k_int.gen.Z39_50_APDU_1995.ElementSetName_codec;
import java.lang.String;
import com.k_int.gen.Z39_50_APDU_1995.IntUnit_codec;
import com.k_int.gen.Z39_50_APDU_1995.IntUnit_type;
import com.k_int.gen.Z39_50_APDU_1995.Unit_codec;
import com.k_int.gen.Z39_50_APDU_1995.Unit_type;
import com.k_int.gen.Z39_50_APDU_1995.StringOrNumeric_codec;
import com.k_int.gen.Z39_50_APDU_1995.StringOrNumeric_type;
import com.k_int.gen.Z39_50_APDU_1995.Specification_codec;
import com.k_int.gen.Z39_50_APDU_1995.Specification_type;
import com.k_int.gen.Z39_50_APDU_1995.InternationalString_codec;
import java.lang.String;
import com.k_int.gen.Z39_50_APDU_1995.AttributeList_codec;
import java.util.Vector;
import com.k_int.gen.Z39_50_APDU_1995.AttributeElement_codec;
import com.k_int.gen.Z39_50_APDU_1995.AttributeElement_type;

public class AccessRestrictionsItem128_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static AccessRestrictionsItem128_codec me = null;

    private Integer_codec i_integer_codec = Integer_codec.getCodec();

    private HumanString_codec i_humanstring_codec = HumanString_codec.getCodec();

    private accessChallenges_inline129_codec i_accesschallenges_inline129_codec = accessChallenges_inline129_codec.getCodec();

    public static synchronized AccessRestrictionsItem128_codec getCodec() {
        if (me == null) {
            me = new AccessRestrictionsItem128_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        AccessRestrictionsItem128_type retval = (AccessRestrictionsItem128_type) type_instance;
        if (sm.sequenceBegin()) {
            if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
                retval = new AccessRestrictionsItem128_type();
            }
            retval.accessType = (BigInteger) sm.explicit_tag(i_integer_codec, retval.accessType, 128, 0, false, "accessType");
            retval.accessText = (java.util.Vector) sm.implicit_tag(i_humanstring_codec, retval.accessText, 128, 1, true, "accessText");
            retval.accessChallenges = (java.util.Vector) sm.implicit_tag(i_accesschallenges_inline129_codec, retval.accessChallenges, 128, 2, true, "accessChallenges");
            sm.sequenceEnd();
        }
        return retval;
    }
}
