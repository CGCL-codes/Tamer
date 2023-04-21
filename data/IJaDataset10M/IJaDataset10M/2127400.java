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

public class attributeValues_inline133_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static attributeValues_inline133_codec me = null;

    private static Object[][] choice_info = { { SerializationManager.IMPLICIT, new Integer(128), new Integer(3), NULL_codec.getCodec(), "any_or_none", new Integer(0) }, { SerializationManager.IMPLICIT, new Integer(128), new Integer(4), specific_inline134_codec.getCodec(), "specific", new Integer(1) } };

    public static synchronized attributeValues_inline133_codec getCodec() {
        if (me == null) {
            me = new attributeValues_inline133_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        attributeValues_inline133_type retval = (attributeValues_inline133_type) type_instance;
        if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
            retval = new attributeValues_inline133_type();
            retval = (attributeValues_inline133_type) sm.choice(retval, choice_info, type_name);
        } else {
            if (retval != null) sm.choice(retval, choice_info, type_name);
        }
        if ((retval == null) && (!is_optional)) {
            cat.info("Missing mandatory choice for " + type_name);
        }
        return retval;
    }
}
