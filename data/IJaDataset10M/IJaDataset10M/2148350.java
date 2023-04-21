package dsb.mbc.autogen;

import com4j.*;

@IID("{EA56ACC9-16DB-41E0-8027-7D63F95BD3B1}")
public interface TimesheetLine___v1 extends Com4jObject {

    @VTID(7)
    java.lang.String brokenRules();

    @VTID(8)
    dsb.mbc.autogen.TimesheetLineHours___v1 castToTimesheetLineHours();

    @VTID(9)
    dsb.mbc.autogen.TimesheetLineProducts___v1 castToTimesheetLineProducts();

    @VTID(10)
    java.lang.String costCentreKey();

    @VTID(11)
    java.lang.String costUnitKey();

    @VTID(12)
    java.lang.String customerKey();

    @VTID(13)
    void delete();

    @VTID(14)
    java.lang.String description();

    @VTID(15)
    java.util.Date entryDate();

    @VTID(16)
    java.lang.String hourTypeKey();

    @VTID(17)
    boolean isChanged();

    @VTID(18)
    boolean isDeclarable();

    @VTID(19)
    boolean isDeleted();

    @VTID(20)
    boolean isValid();

    @VTID(21)
    short linePosition();

    @VTID(22)
    dsb.mbc.autogen.TimesheetLineTypes lineType();

    @VTID(23)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal price();

    @VTID(24)
    double pricePer();

    @VTID(30)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal costPrice();

    @VTID(25)
    java.lang.String productKey();

    @VTID(26)
    java.lang.String projectKey();

    @VTID(29)
    java.lang.String subProjectKey();

    @VTID(27)
    double quantity();

    @VTID(28)
    short weekDayNumber();
}
