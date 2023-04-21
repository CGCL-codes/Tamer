package com.grubby.databean.constraints.annotations;

import com.grubby.databean.annotation.SqlTableName;
import com.grubby.databean.constraints.annotation.Length;

/**
 * This is a bean used to test the validation annotations in the unit test suite
 * A. HARMEL-LAW. E: andrew.harmel.law@gmail.com
 * @version 0.1
 */
public class AnotherDummyValidatedBean {

    private String stringOfSomeLength;

    public AnotherDummyValidatedBean() {
    }

    public String getStringOfSomeLength() {
        return stringOfSomeLength;
    }

    @Length(10)
    public void setStringOfSomeLength(String stringOfSomeLength) {
        this.stringOfSomeLength = stringOfSomeLength;
    }
}
