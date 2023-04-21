package dsb.mbc.autogen;

import com4j.*;

@IID("{426BC7D5-667A-11D5-8400-00A0C9CA15AD}")
public interface _Stock extends Com4jObject {

    @VTID(7)
    void delete();

    @VTID(8)
    void undo();

    @VTID(9)
    boolean isNew();

    @VTID(10)
    boolean isDeleted();

    @VTID(11)
    boolean isChanged();

    @VTID(12)
    boolean isValid();

    @VTID(13)
    java.lang.String brokenRules();

    @VTID(14)
    dsb.mbc.autogen.TextList___v1 warehouseIDs();

    @VTID(14)
    @ReturnValue(defaultPropertyThrough = { dsb.mbc.autogen.TextList___v1.class })
    java.lang.String warehouseIDs(@MarshalAs(NativeType.VARIANT) java.lang.Object key);

    @VTID(15)
    void warehouseID(java.lang.String rhs);

    @VTID(16)
    java.lang.String warehouseID();

    @VTID(17)
    void location(java.lang.String rhs);

    @VTID(18)
    java.lang.String location();

    @VTID(19)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal technicalStock();

    @VTID(20)
    void minimumStock(@MarshalAs(NativeType.Currency) java.math.BigDecimal rhs);

    @VTID(21)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal minimumStock();

    @VTID(22)
    void maximumStock(@MarshalAs(NativeType.Currency) java.math.BigDecimal rhs);

    @VTID(23)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal maximumStock();

    @VTID(24)
    void xml(java.lang.String rhs);

    @VTID(25)
    java.lang.String xml();
}
