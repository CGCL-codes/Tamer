package com.em.validation.client.model.defects.defect_040;

import com.em.validation.client.constraints.NotEmpty;

public class Person_040 {

    @NotEmpty
    private String name;

    public Person_040() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpperCaseName() {
        return name.toUpperCase();
    }
}
