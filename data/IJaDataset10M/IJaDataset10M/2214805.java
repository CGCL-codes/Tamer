package org.xorm.tests.model;

public abstract class StringClass extends StringClassBase {

    public String setAndGet() {
        setStringAttribute("Test");
        return getStringAttribute();
    }
}
