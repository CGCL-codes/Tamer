package com.uk.myclasses;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

public class ATable extends Table {

    @Override
    protected String formatPropertyValue(Object rowId, Object colId, Property property) {
        Object v = property.getValue();
        System.out.println("ATable.formatPropertyValue()");
        System.out.println(v);
        System.out.println(colId);
        if (v instanceof Date) {
            Date dateValue = (Date) v;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            return sdf.format(dateValue);
        }
        return super.formatPropertyValue(rowId, colId, property);
    }
}
