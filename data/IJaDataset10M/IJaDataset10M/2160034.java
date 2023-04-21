package com.k_int.gen.UserInfoFormat_searchResult_1;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;
import com.k_int.gen.Z39_50_APDU_1995.DatabaseName_codec;
import java.lang.String;
import com.k_int.gen.Z39_50_APDU_1995.Term_codec;
import com.k_int.gen.Z39_50_APDU_1995.Term_type;
import com.k_int.gen.Z39_50_APDU_1995.Query_codec;
import com.k_int.gen.Z39_50_APDU_1995.Query_type;
import com.k_int.gen.Z39_50_APDU_1995.IntUnit_codec;
import com.k_int.gen.Z39_50_APDU_1995.IntUnit_type;
import com.k_int.gen.Z39_50_APDU_1995.InternationalString_codec;
import java.lang.String;

public class databases_inline191_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static databases_inline191_codec me = null;

    private static Object[][] choice_info = { { SerializationManager.IMPLICIT, new Integer(128), new Integer(1), NULL_codec.getCodec(), "all", new Integer(0) }, { SerializationManager.IMPLICIT, new Integer(128), new Integer(2), list_inline192_codec.getCodec(), "list", new Integer(1) } };

    public static synchronized databases_inline191_codec getCodec() {
        if (me == null) {
            me = new databases_inline191_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        databases_inline191_type retval = (databases_inline191_type) type_instance;
        if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
            retval = new databases_inline191_type();
            retval = (databases_inline191_type) sm.choice(retval, choice_info, type_name);
        } else {
            if (retval != null) sm.choice(retval, choice_info, type_name);
        }
        if ((retval == null) && (!is_optional)) {
            cat.info("Missing mandatory choice for " + type_name);
        }
        return retval;
    }
}
