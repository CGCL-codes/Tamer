package com.assistsoft.swift.field;

import java.util.GregorianCalendar;
import com.assistsoft.swift.annotations.FieldFormat;

public class SWIFTField_98A extends SWIFTField_98AC {

    private static final String TAG = "98A";

    @FieldFormat("//8!n")
    private GregorianCalendar date;

    public SWIFTField_98A() {
        super(TAG);
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }
}
