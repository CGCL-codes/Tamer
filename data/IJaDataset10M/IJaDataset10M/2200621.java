package com.k_int.gen.Z39_50_APDU_1995;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;

public class DiagRec_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static DiagRec_codec me = null;

    private static Object[][] choice_info = { { SerializationManager.TAGMODE_NONE, new Integer(-1), new Integer(-1), DefaultDiagFormat_codec.getCodec(), "defaultFormat", new Integer(0) }, { SerializationManager.TAGMODE_NONE, new Integer(-1), new Integer(-1), EXTERNAL_codec.getCodec(), "externallyDefined", new Integer(1) } };

    public static synchronized DiagRec_codec getCodec() {
        if (me == null) {
            me = new DiagRec_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        DiagRec_type retval = (DiagRec_type) type_instance;
        if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
            retval = new DiagRec_type();
            retval = (DiagRec_type) sm.choice(retval, choice_info, type_name);
        } else {
            if (retval != null) sm.choice(retval, choice_info, type_name);
        }
        if ((retval == null) && (!is_optional)) {
            cat.info("Missing mandatory choice for " + type_name);
        }
        return retval;
    }
}
