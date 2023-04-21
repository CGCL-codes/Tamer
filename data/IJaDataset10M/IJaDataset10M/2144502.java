package dsb.mbc.autogen;

import com4j.*;

@IID("{18D941F8-38DB-42E2-B880-52A5C0E044E6}")
public interface _FteVatEntry extends Com4jObject {

    @VTID(7)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal turnoverAmount();

    @VTID(8)
    void turnoverAmount(@MarshalAs(NativeType.Currency) java.math.BigDecimal rhs);

    @VTID(9)
    java.lang.String vatCodeKey();

    @VTID(10)
    void vatCodeKey(java.lang.String rhs);

    @VTID(11)
    java.lang.String vatCode();

    @VTID(12)
    dsb.mbc.autogen.TextList___v1 vatCodeList();

    @VTID(12)
    @ReturnValue(defaultPropertyThrough = { dsb.mbc.autogen.TextList___v1.class })
    java.lang.String vatCodeList(@MarshalAs(NativeType.VARIANT) java.lang.Object key);

    @VTID(13)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal vatAmount();

    @VTID(14)
    void vatAmount(@MarshalAs(NativeType.Currency) java.math.BigDecimal rhs);

    @VTID(15)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal notDeductableAmount();

    @VTID(16)
    void notDeductableAmount(@MarshalAs(NativeType.Currency) java.math.BigDecimal rhs);

    @VTID(17)
    dsb.mbc.autogen.FteVatTypes vatType();

    @VTID(18)
    void vatType(dsb.mbc.autogen.FteVatTypes rhs);

    @VTID(19)
    java.lang.String brokenRules();

    @VTID(20)
    boolean isValid();

    @VTID(21)
    boolean isDeleted();

    @VTID(22)
    int childIndex();

    @VTID(23)
    boolean eventsEnabled();

    @VTID(24)
    void eventsEnabled(boolean rhs);

    @VTID(25)
    void delete();

    @VTID(26)
    void reCalculateVat();
}
