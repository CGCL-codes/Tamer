package org.nakedobjects.object;

import org.nakedobjects.application.valueholder.FloatingPointNumber;

public class ObjectContainingFloatingPointNumber {

    private final FloatingPointNumber number = new FloatingPointNumber();

    public FloatingPointNumber getValue() {
        return number;
    }
}
