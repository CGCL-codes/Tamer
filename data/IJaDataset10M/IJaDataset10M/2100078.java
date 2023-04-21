package com.k_int.gen.Z39_50_APDU_1995;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;

public class Operator_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static Operator_codec me = null;

    private static Object[][] choice_info = { { SerializationManager.IMPLICIT, new Integer(128), new Integer(0), NULL_codec.getCodec(), "and", new Integer(0) }, { SerializationManager.IMPLICIT, new Integer(128), new Integer(1), NULL_codec.getCodec(), "or", new Integer(1) }, { SerializationManager.IMPLICIT, new Integer(128), new Integer(2), NULL_codec.getCodec(), "and_not", new Integer(2) }, { SerializationManager.IMPLICIT, new Integer(128), new Integer(3), ProximityOperator_codec.getCodec(), "prox", new Integer(3) } };

    public static synchronized Operator_codec getCodec() {
        if (me == null) {
            me = new Operator_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        Operator_type retval = (Operator_type) type_instance;
        if (sm.constructedBegin(128, 46)) {
            if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
                retval = new Operator_type();
                retval = (Operator_type) sm.choice(retval, choice_info, type_name);
            } else {
                if (retval != null) sm.choice(retval, choice_info, type_name);
            }
            if ((retval == null) && (!is_optional)) {
                cat.info("Missing mandatory choice for " + type_name);
            }
        }
        sm.constructedEnd();
        return retval;
    }
}
