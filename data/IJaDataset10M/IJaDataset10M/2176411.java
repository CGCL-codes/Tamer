package org.conann.container.examples;

import javax.webbeans.Initializer;

public class ClassWithAnnotatedConstructor extends BaseClassForConstruction {

    @Initializer
    public ClassWithAnnotatedConstructor(String value) {
        setProperlyConstructed();
    }

    public ClassWithAnnotatedConstructor(Integer value) {
    }
}
