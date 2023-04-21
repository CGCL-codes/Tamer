package com.k_int.gen.Z39_50_APDU_1995;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;

public class Records_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static Records_codec me = null;

    private static Object[][] choice_info = { { SerializationManager.IMPLICIT, new Integer(128), new Integer(28), responseRecords_inline12_codec.getCodec(), "responseRecords", new Integer(0) }, { SerializationManager.IMPLICIT, new Integer(128), new Integer(130), DefaultDiagFormat_codec.getCodec(), "nonSurrogateDiagnostic", new Integer(1) }, { SerializationManager.IMPLICIT, new Integer(128), new Integer(205), multipleNonSurDiagnostics_inline13_codec.getCodec(), "multipleNonSurDiagnostics", new Integer(2) } };

    public static synchronized Records_codec getCodec() {
        if (me == null) {
            me = new Records_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        Records_type retval = (Records_type) type_instance;
        if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
            retval = new Records_type();
            retval = (Records_type) sm.choice(retval, choice_info, type_name);
        } else {
            if (retval != null) sm.choice(retval, choice_info, type_name);
        }
        if ((retval == null) && (!is_optional)) {
            cat.info("Missing mandatory choice for " + type_name);
        }
        return retval;
    }
}
