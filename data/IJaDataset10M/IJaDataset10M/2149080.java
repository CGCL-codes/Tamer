package com.jguild.devportal.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class EnumTypeConverter implements Converter {

    @SuppressWarnings({ "unchecked" })
    public Object getAsObject(final FacesContext context, final UIComponent comp, final String value) throws ConverterException {
        final Class enumType = comp.getValueExpression("value").getType(context.getELContext());
        return Enum.valueOf(enumType, value);
    }

    public String getAsString(final FacesContext context, final UIComponent component, final Object object) throws ConverterException {
        if (object == null) {
            return null;
        }
        final Enum type = (Enum) object;
        return type.toString();
    }
}
