package dsb.mbc.autogen;

import com4j.*;

@IID("{BF8DEEC2-7F7B-4AFD-80B0-DD12B7C2FCD9}")
public interface _CustomTable extends Com4jObject {

    @VTID(7)
    boolean isChanged();

    @VTID(8)
    boolean isValid();

    @VTID(9)
    @ReturnValue(type = NativeType.VARIANT)
    java.lang.Object recordCount();

    @VTID(10)
    java.lang.String tableName();

    @VTID(11)
    void xml(java.lang.String rhs);

    @VTID(12)
    java.lang.String xml();

    @VTID(13)
    dsb.mbc.autogen.CustomTableRecord___v0 addRecord();

    @VTID(14)
    @DefaultMethod
    dsb.mbc.autogen.CustomTableRecord___v0 item(int index);

    @VTID(15)
    Com4jObject newEnum();

    @VTID(16)
    void undo();
}
