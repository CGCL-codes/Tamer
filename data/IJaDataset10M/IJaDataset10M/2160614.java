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

public class AttributeCombination_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static AttributeCombination_codec me = null;

    private AttributeOccurrence_codec i_attributeoccurrence_codec = AttributeOccurrence_codec.getCodec();

    public static synchronized AttributeCombination_codec getCodec() {
        if (me == null) {
            me = new AttributeCombination_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        java.util.Vector retval = (java.util.Vector) type_instance;
        if (sm.sequenceBegin()) {
            if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
                retval = new java.util.Vector();
            }
            retval = sm.sequenceOf(retval, i_attributeoccurrence_codec);
            sm.sequenceEnd();
        }
        return retval;
    }
}
