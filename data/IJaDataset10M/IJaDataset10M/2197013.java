package com.k_int.gen.Z39_50_APDU_1995;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;

public class occurrences_inline31_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static occurrences_inline31_codec me = null;

    private static Object[][] choice_info = { { SerializationManager.EXPLICIT, new Integer(128), new Integer(2), Integer_codec.getCodec(), "global", new Integer(0) }, { SerializationManager.IMPLICIT, new Integer(128), new Integer(3), byDatabase_inline32_codec.getCodec(), "byDatabase", new Integer(1) } };

    public static synchronized occurrences_inline31_codec getCodec() {
        if (me == null) {
            me = new occurrences_inline31_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        occurrences_inline31_type retval = (occurrences_inline31_type) type_instance;
        if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
            retval = new occurrences_inline31_type();
            retval = (occurrences_inline31_type) sm.choice(retval, choice_info, type_name);
        } else {
            if (retval != null) sm.choice(retval, choice_info, type_name);
        }
        if ((retval == null) && (!is_optional)) {
            cat.info("Missing mandatory choice for " + type_name);
        }
        return retval;
    }
}
