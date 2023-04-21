package com.k_int.codec.runtime;

public class BOOL_codec extends base_codec {

    private static BOOL_codec me = null;

    public static BOOL_codec getCodec() {
        if (me == null) me = new BOOL_codec();
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        Boolean retval = (Boolean) type_instance;
        boolean is_constructed = false;
        if (sm.getDirection() == SerializationManager.DIRECTION_ENCODE) {
            if (null != retval) {
                sm.implicit_settag(SerializationManager.UNIVERSAL, SerializationManager.BOOLEAN);
                int len = sm.tag_codec(false);
                if (len >= 0) retval = sm.boolean_codec(retval, is_constructed);
            }
        } else if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
            sm.implicit_settag(SerializationManager.UNIVERSAL, SerializationManager.BOOLEAN);
            int len = sm.tag_codec(false);
            if (len >= 0) retval = sm.boolean_codec(retval, is_constructed);
        }
        if ((retval == null) && (!is_optional)) throw new java.io.IOException("Missing mandatory member: " + type_name);
        return (Object) retval;
    }
}
