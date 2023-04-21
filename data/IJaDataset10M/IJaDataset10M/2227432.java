package org.elascript.interpreter;

import java.lang.reflect.*;

public class ArrayElementResult extends LValueResult {

    private Object array;

    private int index;

    public ArrayElementResult(Object array, int index) {
        this.array = array;
        this.index = index;
    }

    @Override
    public void setValue(Object value) throws AccessException {
        try {
            Array.set(array, index, value);
        } catch (Exception e) {
            throw new AccessException(e.getMessage());
        }
    }

    @Override
    public Object getValue() throws AccessException {
        try {
            return Array.get(array, index);
        } catch (Exception e) {
            throw new AccessException(e.getMessage());
        }
    }
}
