package dsb.mbc.autogen;

import com4j.*;

@IID("{21F8193D-567C-4799-A169-7008C03F8535}")
public interface Account___v7 extends Com4jObject {

    @VTID(7)
    void connect(dsb.mbc.autogen.Administration___v20 administration);

    @VTID(8)
    dsb.mbc.autogen.Administration___v20 administration();

    @VTID(9)
    void clear();

    @VTID(10)
    void load(Holder<java.lang.String> accountID);

    @VTID(11)
    void save();

    @VTID(12)
    void delete();

    @VTID(13)
    void undo();

    @VTID(64)
    boolean exists(java.lang.String accountID);

    @VTID(14)
    java.lang.String database();

    @VTID(15)
    short fiscalYear();

    @VTID(16)
    boolean isNew();

    @VTID(17)
    boolean isDeleted();

    @VTID(18)
    boolean isChanged();

    @VTID(19)
    boolean isValid();

    @VTID(20)
    java.lang.String brokenRules();

    @VTID(21)
    dsb.mbc.autogen.TextList___v1 vatCodeIDs();

    @VTID(21)
    @ReturnValue(defaultPropertyThrough = { dsb.mbc.autogen.TextList___v1.class })
    java.lang.String vatCodeIDs(@MarshalAs(NativeType.VARIANT) java.lang.Object key);

    @VTID(22)
    void accountID(java.lang.String rhs);

    @VTID(23)
    java.lang.String accountID();

    @VTID(24)
    void shortName(java.lang.String rhs);

    @VTID(25)
    java.lang.String shortName();

    @VTID(26)
    void description(java.lang.String rhs);

    @VTID(27)
    java.lang.String description();

    @VTID(28)
    void accountType(dsb.mbc.autogen.AccountTypes rhs);

    @VTID(29)
    dsb.mbc.autogen.AccountTypes accountType();

    @VTID(30)
    void specifyCostCentre(boolean rhs);

    @VTID(31)
    boolean specifyCostCentre();

    @VTID(32)
    void specifyCostUnit(boolean rhs);

    @VTID(33)
    boolean specifyCostUnit();

    @VTID(34)
    void budgetType(dsb.mbc.autogen.BudgetTypes rhs);

    @VTID(35)
    dsb.mbc.autogen.BudgetTypes budgetType();

    @VTID(36)
    void blocked(boolean rhs);

    @VTID(37)
    boolean blocked();

    @VTID(38)
    void subTotalLevel(short rhs);

    @VTID(39)
    short subTotalLevel();

    @VTID(40)
    void percNotDeductable(@MarshalAs(NativeType.Currency) java.math.BigDecimal rhs);

    @VTID(41)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal percNotDeductable();

    @VTID(42)
    void vatCodeID(java.lang.String rhs);

    @VTID(43)
    java.lang.String vatCodeID();

    @VTID(75)
    void vatCodeKey(java.lang.String rhs);

    @VTID(76)
    java.lang.String vatCodeKey();

    @VTID(44)
    void vatType(dsb.mbc.autogen.VatTypes rhs);

    @VTID(45)
    dsb.mbc.autogen.VatTypes vatType();

    @VTID(77)
    void condensed(boolean rhs);

    @VTID(78)
    boolean condensed();

    @VTID(46)
    void percVatNotDeductable(@MarshalAs(NativeType.Currency) java.math.BigDecimal rhs);

    @VTID(47)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal percVatNotDeductable();

    @VTID(48)
    void hints(java.lang.String rhs);

    @VTID(49)
    java.lang.String hints();

    @VTID(50)
    int pageNumber();

    @VTID(51)
    int lineNumber();

    @VTID(52)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal startBalance();

    @VTID(53)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal currentBalance();

    @VTID(54)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal currentTotalDebit();

    @VTID(55)
    @ReturnValue(type = NativeType.Currency)
    java.math.BigDecimal currentTotalCredit();

    @VTID(56)
    void debitOrCreditPreference(java.lang.String rhs);

    @VTID(57)
    java.lang.String debitOrCreditPreference();

    @VTID(58)
    void xml(java.lang.String rhs);

    @VTID(59)
    java.lang.String xml();

    @VTID(60)
    dsb.mbc.autogen._CustomFields customFields();

    @VTID(61)
    void load_ByVal(java.lang.String accountID);

    @VTID(62)
    boolean isProjectAccount();

    @VTID(63)
    void isProjectAccount(boolean rhs);

    @VTID(65)
    void defaultCostCentreKey(java.lang.String rhs);

    @VTID(66)
    java.lang.String defaultCostCentreKey();

    @VTID(67)
    dsb.mbc.autogen.TextList___v1 costCentreIDs();

    @VTID(67)
    @ReturnValue(defaultPropertyThrough = { dsb.mbc.autogen.TextList___v1.class })
    java.lang.String costCentreIDs(@MarshalAs(NativeType.VARIANT) java.lang.Object key);

    @VTID(68)
    void defaultCostCentreID(java.lang.String rhs);

    @VTID(69)
    java.lang.String defaultCostCentreID();

    @VTID(70)
    void defaultCostUnitKey(java.lang.String rhs);

    @VTID(71)
    java.lang.String defaultCostUnitKey();

    @VTID(72)
    dsb.mbc.autogen.TextList___v1 costUnitIDs();

    @VTID(72)
    @ReturnValue(defaultPropertyThrough = { dsb.mbc.autogen.TextList___v1.class })
    java.lang.String costUnitIDs(@MarshalAs(NativeType.VARIANT) java.lang.Object key);

    @VTID(73)
    void defaultCostUnitID(java.lang.String rhs);

    @VTID(74)
    java.lang.String defaultCostUnitID();
}
