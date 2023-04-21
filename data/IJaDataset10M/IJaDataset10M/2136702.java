package org.colombbus.tangara.commons.resinject;

import java.awt.Font;

class FontConverter implements ResourceConverter {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(String stringFormat, Class<?> resourceClass) {
        Font typedValue = Font.decode(stringFormat);
        return (T) typedValue;
    }
}
