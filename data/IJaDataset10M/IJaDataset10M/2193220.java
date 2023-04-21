package dsb.mbc.autogen;

import com4j.*;

@IID("{5DBA970B-857A-11D4-8400-00A0C9CA15AD}")
public interface _VatDeclarationLine extends Com4jObject {

    @VTID(15)
    java.lang.String vatSection();

    @VTID(20)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal amountTurnover();

    @VTID(21)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal amountTurnoverCur();

    @VTID(25)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal amountTurnoverCurX();

    @VTID(23)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal amountVat();

    @VTID(24)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal amountVatCur();

    @VTID(26)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal amountVatCurX();

    @VTID(18)
    short declarationMonth();

    @VTID(19)
    short declarationYear();

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
    short vatCodeIDx();

    @VTID(16)
    int lineID();

    @VTID(17)
    java.util.Date invoiceDate();

    @VTID(22)
    void amountVat(@MarshalAs(NativeType.Currency) java.math.BigDecimal rhs);

    @VTID(27)
    short transactionID();

    @VTID(28)
    java.lang.String description();

    @VTID(7)
    void delete();

    @VTID(8)
    void undo();
}
