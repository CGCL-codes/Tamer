package org.td4j.core.reflect;

import ch.miranet.commons.TK;

public class UnknownPropertyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static String prepareSinglePropertyMsg(Class<?> cls, String propertyName) {
        return String.format("Property not found: %1$s#%2$s", cls.getName(), TK.Strings.assertNotEmpty(propertyName, "propertyName"));
    }

    private static String prepareMultiPropertiesMsg(Class<?> cls, String... propertyNames) {
        TK.Arrays.assertNotEmpty(propertyNames, "propertyNames");
        final StringBuilder sb = new StringBuilder("Properties not found " + cls.getName() + ": ");
        boolean firstElement = true;
        for (String pName : propertyNames) {
            if (!firstElement) sb.append(", ");
            firstElement = false;
            sb.append(String.format("#%1$s", pName));
        }
        return sb.toString();
    }

    public UnknownPropertyException(Class<?> cls, String... propertyNames) {
        super(propertyNames.length > 1 ? prepareMultiPropertiesMsg(cls, propertyNames) : prepareSinglePropertyMsg(cls, propertyNames[0]));
    }
}
