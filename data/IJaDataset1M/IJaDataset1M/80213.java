package net.sf.lightbound.opencms.content;

import net.sf.lightbound.exceptions.ConversionException;
import net.sf.lightbound.extend.StringValueConverter;

public class ContentContainerPrinter implements StringValueConverter {

    public String convertToString(Object value) {
        if (value instanceof ContentContainerAssociation) {
            return ((ContentContainerAssociation) value).getStringValue();
        }
        return null;
    }

    public Object parseFrom(String value, Class<?> paramType) throws ConversionException {
        return null;
    }
}
