package com.em.validation.client.model.validator;

import javax.validation.Valid;
import javax.validation.constraints.Size;

public class ClassWithArray {

    @Size(min = 1, message = "The test array needs a size greater than {min}")
    @Valid
    private ChildNode[] testArray = new ChildNode[] {};

    public ChildNode[] getTestArray() {
        return testArray;
    }

    public void setTestArray(ChildNode[] testArray) {
        this.testArray = testArray;
    }
}
