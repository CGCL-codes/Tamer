package org.powermock.reflect.testclasses;

public class ClassWithOverloadedMethods {

    public String overloaded(double value, Parent parent) {
        return "parent";
    }

    public String overloaded(double value, Child child) {
        return "child";
    }
}
