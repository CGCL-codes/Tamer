package ru.spb.osll.report;

import org.xml.sax.Attributes;

public class Item implements IItem {

    String type;

    String title;

    double price;

    double weight;

    String units;

    int quantity;

    public Item(Attributes atts) {
        type = atts.getValue(TYPE);
        title = atts.getValue(TITLE);
        price = ReportUtil.getDoubleAttr(atts, PRICE);
        weight = ReportUtil.getDoubleAttr(atts, WEIGHT);
        units = atts.getValue(UNITS);
        quantity = ReportUtil.getIntegerAttr(atts, QUANTITY);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n---item---");
        sb.append("\ntype : " + type);
        sb.append("\ntitle : " + title);
        sb.append("\nprice : " + price);
        if (weight != -1.0) sb.append("\nweight : " + weight);
        if (units != null) sb.append("\nunits : " + units);
        if (quantity != -1) sb.append("\nquantity : " + quantity);
        return sb.toString();
    }
}

interface IItem {

    String TYPE = "type";

    String TITLE = "title";

    String PRICE = "price";

    String WEIGHT = "weight";

    String UNITS = "units";

    String QUANTITY = "quantity";
}
