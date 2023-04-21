package com.advancedpwr.record.methods;

import java.util.Date;

public class DateBuilder extends BuildMethodWriter {

    protected void writePopulators() {
    }

    protected void writeInstance() {
        Date date = (Date) getAccessPath().getResult();
        writeLine(instanceName() + " = new " + instanceType() + "( " + date.getTime() + "l )");
    }

    protected String instanceType() {
        Class resultClass = getAccessPath().getResultClass();
        if (java.sql.Date.class.equals(resultClass)) {
            return resultClass.getName();
        }
        return resultClass.getSimpleName();
    }
}
