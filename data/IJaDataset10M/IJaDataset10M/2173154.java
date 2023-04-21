package nu.esox.accounting;

import org.xml.sax.*;
import nu.esox.util.*;
import nu.esox.xml.*;

@SuppressWarnings("serial")
public class Account extends NamedAndNumbered implements XmlWriter.Writeable {

    public static final String PROPERTY_AMOUNT = "PROPERTY_AMOUNT";

    public static final String PROPERTY_TYPE = "PROPERTY_TYPE";

    public static final String PROPERTY_IB = "PROPERTY_IB";

    public static final String PROPERTY_BUDGET = "PROPERTY_BUDGET";

    public static final String PROPERTY_LOCKED = "PROPERTY_LOCKED";

    public static final Type TYPE_UNDEFINED = new Type("?", "undefined", true, true);

    public static final Type TYPE_TURNOVER_ASSET = new Type("Omsättningstillgångar", "turnover-asset", true, false);

    public static final Type TYPE_FACILITY_ASSET = new Type("Anläggningstillgångar", "facility-asset", true, false);

    public static final Type TYPE_SHORT_DEPT = new Type("Kortfristiga skulder", "short-dept", true, false);

    public static final Type TYPE_LONG_DEPT = new Type("Långfristiga skulder", "long-dept", true, false);

    public static final Type TYPE_OWN_CAPITAL = new Type("Eget kapital", "own-capital", true, false);

    public static final Type TYPE_SALES_REVENUE = new Type("Försäljningsintäkter", "sales-revenue", false, true);

    public static final Type TYPE_OTHER_REVENUE = new Type("Övriga rörelseintäkter", "other-revenue", false, true);

    public static final Type TYPE_MATERIAL_EXPENSES = new Type("Materialkostnader", "material-expenses", false, true);

    public static final Type TYPE_OTHER_EXPENSES = new Type("Övriga rörelsekostnader", "other-expenses", false, true);

    public static final Type TYPE_WRITE_OFFS = new Type("Avskrivningar", "write-offs", false, true);

    public static final Type TYPE_FINANCIAL_REVENUE = new Type("Finansiella intäkter", "financial-revenue", false, true);

    public static final Type TYPE_FINANCIAL_EXPENSES = new Type("Finansiella kostnader", "financial-expenses", false, true);

    public static final Type TYPE_EXTRAORDINARY_RESULT = new Type("Extraordinära intäkt/kostn", "extraordinary-result", false, true);

    public static final Type TYPE_RESULT = new Type("Årets resultat", "result", false, true);

    public static final Type[] TYPES = { TYPE_UNDEFINED, TYPE_TURNOVER_ASSET, TYPE_FACILITY_ASSET, TYPE_SHORT_DEPT, TYPE_LONG_DEPT, TYPE_OWN_CAPITAL, TYPE_SALES_REVENUE, TYPE_OTHER_REVENUE, TYPE_MATERIAL_EXPENSES, TYPE_OTHER_EXPENSES, TYPE_WRITE_OFFS, TYPE_FINANCIAL_REVENUE, TYPE_FINANCIAL_EXPENSES, TYPE_EXTRAORDINARY_RESULT, TYPE_RESULT };

    private boolean m_locked = false;

    private Type m_type = TYPE_UNDEFINED;

    private double m_ib = 0;

    private double m_budget = 0;

    private final TransactionSet m_transactions = new TransactionSet();

    public Account(int number) {
        this(number, "");
    }

    public Account(int number, String name) {
        setNumber(number);
        setName(name);
        ObservableListener l = new ObservableListener() {

            public void valueChanged(ObservableEvent ev) {
                if (ev.getInfo() == TransactionSet.PROPERTY_AMOUNT) fireValueChanged(PROPERTY_AMOUNT, ev.getData());
            }
        };
        m_transactions.addObservableListener(l);
    }

    public Account newYear() {
        Account a = new Account(getNumber(), getName());
        a.m_type = m_type;
        if (hasIb()) a.setIb(getAmount());
        return a;
    }

    public final TransactionSet getTransactions() {
        return m_transactions;
    }

    public final Type getType() {
        return m_type;
    }

    public double getIb() {
        return m_ib;
    }

    public double getBudget() {
        return m_budget;
    }

    public double getAmount() {
        double tmp = m_transactions.getAmount();
        if (Transaction.isAmountUndefined(tmp)) tmp = 0;
        if (hasIb()) tmp = Transaction.addAmounts(tmp, m_ib);
        return tmp;
    }

    public final boolean hasIb() {
        return m_type.hasIb();
    }

    public final boolean hasBudget() {
        return m_type.hasBudget();
    }

    public final boolean isLocked() {
        return m_locked;
    }

    public final void setLocked(boolean locked) {
        if (m_locked == locked) return;
        m_locked = locked;
        fireValueChanged(PROPERTY_LOCKED, (Boolean) m_locked);
    }

    public final void setType(Type type) {
        if (m_type == type) return;
        m_type = type;
        fireValueChanged(PROPERTY_TYPE, m_type);
    }

    public final void setIb(double ib) {
        assert ib == 0 || hasIb();
        if (m_ib == ib) return;
        m_ib = ib;
        fireValueChanged(PROPERTY_IB, m_ib);
        fireValueChanged(PROPERTY_AMOUNT, null);
    }

    public final void setBudget(double budget) {
        if (m_budget == budget) return;
        m_budget = budget;
        fireValueChanged(PROPERTY_BUDGET, m_budget);
    }

    public String xmlGetTag() {
        return "account";
    }

    public void xmlWriteAttributes(XmlWriter w) {
        w.write("number", getNumber());
        w.write("name", getName());
        w.write("ib", Transaction.formatAmount(m_ib));
        w.write("budget", Transaction.formatAmount(m_budget));
    }

    public void xmlWriteSubmodels(XmlWriter w) {
        w.write(m_type);
        if (!m_transactions.isEmpty()) w.write(m_transactions);
    }

    Account(Attributes as) throws java.text.ParseException {
        this(XmlReader.xml2Integer(as, "number"));
        setName(XmlReader.xml2String(as, "name"));
        m_ib = XmlReader.xml2Double(as, "ib", 0);
        m_budget = XmlReader.xml2Double(as, "budget", 0);
    }

    public TransactionSet xmlCreate_transactions(Object superModel, Attributes a) {
        return m_transactions;
    }

    public Type xmlCreate_type_undefined(Object superModel, Attributes a) {
        return m_type = TYPE_UNDEFINED;
    }

    public Type xmlCreate_type_turnover_asset(Object superModel, Attributes a) {
        return m_type = TYPE_TURNOVER_ASSET;
    }

    public Type xmlCreate_type_facility_asset(Object superModel, Attributes a) {
        return m_type = TYPE_FACILITY_ASSET;
    }

    public Type xmlCreate_type_short_dept(Object superModel, Attributes a) {
        return m_type = TYPE_SHORT_DEPT;
    }

    public Type xmlCreate_type_long_dept(Object superModel, Attributes a) {
        return m_type = TYPE_LONG_DEPT;
    }

    public Type xmlCreate_type_own_capital(Object superModel, Attributes a) {
        return m_type = TYPE_OWN_CAPITAL;
    }

    public Type xmlCreate_type_sales_revenue(Object superModel, Attributes a) {
        return m_type = TYPE_SALES_REVENUE;
    }

    public Type xmlCreate_type_other_revenue(Object superModel, Attributes a) {
        return m_type = TYPE_OTHER_REVENUE;
    }

    public Type xmlCreate_type_material_expenses(Object superModel, Attributes a) {
        return m_type = TYPE_MATERIAL_EXPENSES;
    }

    public Type xmlCreate_type_other_expenses(Object superModel, Attributes a) {
        return m_type = TYPE_OTHER_EXPENSES;
    }

    public Type xmlCreate_type_write_offs(Object superModel, Attributes a) {
        return m_type = TYPE_WRITE_OFFS;
    }

    public Type xmlCreate_type_financial_revenue(Object superModel, Attributes a) {
        return m_type = TYPE_FINANCIAL_REVENUE;
    }

    public Type xmlCreate_type_financial_expenses(Object superModel, Attributes a) {
        return m_type = TYPE_FINANCIAL_EXPENSES;
    }

    public Type xmlCreate_type_extraordinary_result(Object superModel, Attributes a) {
        return m_type = TYPE_EXTRAORDINARY_RESULT;
    }

    public Type xmlCreate_type_result(Object superModel, Attributes a) {
        return m_type = TYPE_RESULT;
    }

    public static final class Type implements XmlWriter.UnsharedWriteable {

        private final String m_name;

        private final String m_id;

        private final boolean m_hasIb;

        private final boolean m_hasBudget;

        private Type(String name, String id, boolean hasIb, boolean hasBudget) {
            m_name = name;
            m_id = id;
            m_hasIb = hasIb;
            m_hasBudget = hasBudget;
        }

        public String getName() {
            return m_name;
        }

        private boolean hasIb() {
            return m_hasIb;
        }

        private boolean hasBudget() {
            return m_hasBudget;
        }

        public String xmlGetTag() {
            return "type-" + m_id;
        }

        public void xmlWriteSubmodels(XmlWriter w) {
        }

        public void xmlWriteAttributes(XmlWriter w) {
        }

        public String toString() {
            return m_name;
        }
    }
}
