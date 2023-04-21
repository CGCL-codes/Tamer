package pspdash.data;

import java.util.Enumeration;

class ProductList extends DataList {

    NumberFunction func;

    double value;

    public ProductList(DataRepository r, String dataName, String prefix, NumberFunction f, String fName) {
        super(r, dataName, prefix, fName);
        func = f;
        recalc();
    }

    public void recalc() {
        if (func == null) return;
        value = 1.0;
        NumberData d;
        Enumeration values = dataList.elements();
        while (values.hasMoreElements()) {
            try {
                d = (NumberData) ((DataListValue) values.nextElement()).value;
            } catch (ClassCastException cce) {
                d = null;
            }
            if (d != null && !Double.isNaN(d.getDouble())) value *= d.getDouble(); else if (re == null) {
                value = Double.NaN;
                break;
            }
        }
        func.recalc();
    }

    public String toString() {
        if (func != null) return "ProductList[" + func.name() + "]"; else return "ProductList[?]";
    }
}
