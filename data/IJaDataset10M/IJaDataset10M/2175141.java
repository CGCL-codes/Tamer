package com.ivis.xprocess.reporting.facades;

import com.ivis.xprocess.core.Nameable;

public class NameableImpl implements Nameable {

    private String myName;

    private String myDescription;

    public NameableImpl(String name) {
        myName = name;
    }

    public String getName() {
        return myName;
    }

    public void setName(String name) {
        myName = name;
    }

    public String getDescription() {
        return myDescription;
    }

    public void setDescription(String description) {
        myDescription = description;
    }
}
