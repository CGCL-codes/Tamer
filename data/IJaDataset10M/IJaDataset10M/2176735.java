package com.em.validation.client.model.defects.defect_069;

import javax.validation.constraints.NotNull;

public class ValidConstraintChildClass {

    @NotNull
    private String fieldOne = null;

    private String fieldTwo = null;

    public String getFieldOne() {
        return fieldOne;
    }

    public void setFieldOne(String fieldOne) {
        this.fieldOne = fieldOne;
    }

    @NotNull
    public String getFieldTwo() {
        return fieldTwo;
    }

    public void setFieldTwo(String fieldTwo) {
        this.fieldTwo = fieldTwo;
    }
}
