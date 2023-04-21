package dsb.mbc.autogen;

import com4j.*;

@IID("{C11998D5-4A4C-49A8-8971-0B9B5E564B7C}")
public interface TextList___v1 extends Com4jObject {

    @VTID(13)
    boolean valueExists(java.lang.String value);

    @VTID(7)
    @DefaultMethod
    java.lang.String item(@MarshalAs(NativeType.VARIANT) java.lang.Object key);

    @VTID(8)
    int count();

    @VTID(9)
    Com4jObject newEnum();

    @VTID(10)
    java.lang.String key(@MarshalAs(NativeType.VARIANT) java.lang.Object text);

    @VTID(11)
    boolean exists(java.lang.String key);

    @VTID(12)
    java.lang.String xml();
}
