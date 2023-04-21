package com.google.gwt.validation.client;

import javax.validation.constraints.NotNull;

/**
 * A simple sample class to test javax.validation.
 */
public class SimpleSample {

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
