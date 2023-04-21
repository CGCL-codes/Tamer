package org.javadm.library.preprocess;

public class ReplaceColumnDataDateToYear extends ReplaceColumnData {

    public ReplaceColumnDataDateToYear(String columnName, String nullValue) {
        super(columnName, nullValue);
    }

    @Override
    public String getUpdatedValue(String value) {
        String year = value.substring(0, 4);
        return year;
    }
}
