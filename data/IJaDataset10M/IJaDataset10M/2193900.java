package com.assistsoft.swift.field;

import com.assistsoft.swift.annotations.FieldFormat;

public class SWIFTField_22F extends AbstractSWIFTField {

    public static final String TAG = "22F";

    @FieldFormat("/[8c]")
    private String dataSourceScheme;

    @FieldFormat("/4!c")
    private String indicator;

    public SWIFTField_22F() {
        super(TAG);
    }

    public String getDataSourceScheme() {
        return dataSourceScheme;
    }

    public void setDataSourceScheme(String dataSourceScheme) {
        this.dataSourceScheme = dataSourceScheme;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }
}
