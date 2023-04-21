package com.volantis.testtools.mock.value;

public interface CompositeExpectedValue extends ExpectedValue {

    void addExpectedValue(ExpectedValue value);
}
