package com.googlecode.bdoc.sandbox.ultratinyruntimeanalyzer;

import java.lang.reflect.Method;

public class AndCallback extends Callback {

    public AndCallback(Class<?> testClass, Scenario scenario) {
        super(testClass, scenario);
    }

    protected void register(Method method) {
        scenario.addAnd(method.getName());
    }
}
