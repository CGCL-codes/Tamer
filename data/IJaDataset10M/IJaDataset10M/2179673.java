package org.javadm.library.preprocess;

public class ReplaceColumnDataDateToMonth extends ReplaceColumnData {

    public ReplaceColumnDataDateToMonth(String columnName, String nullValue) {
        super(columnName, nullValue);
    }

    @Override
    public String getUpdatedValue(String value) {
        String year = value.substring(0, 4);
        String month = value.substring(5, 7);
        return year + month;
    }
}
