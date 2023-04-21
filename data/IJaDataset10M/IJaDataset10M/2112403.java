package org.mgkFramework.db;

public class NumberToBooleanCaster implements ITypeCaster {

    @Override
    public Object cast(Object input) {
        if (input == null) {
            return null;
        }
        int iVal = ((Number) input).intValue();
        return iVal != 0;
    }
}
