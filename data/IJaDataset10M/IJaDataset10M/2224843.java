package com.angel.architecture.flex.serialization.converter;

import java.util.Date;
import com.angel.architecture.exceptions.NonBusinessException;
import com.angel.architecture.flex.date.LocalDate;
import com.angel.architecture.flex.serialization.SerializationStatus;
import com.angel.architecture.flex.serialization.property.PropertyAccessor;

/**
 * La responsabilidad de esta clase es hacer el ida y vuelta de objetos que representan fechas.
 *
 * @author Juan Isern
 * @see com.angel.arquitectura.flex.date.LocalDate
 */
public class DatePropertyConverter implements PropertyConverter, SimpleValueConverter {

    public Object serializeProperty(Object propertyOwner, PropertyAccessor propertyAccessor, SerializationStatus status) {
        Object value = propertyAccessor.get(propertyOwner);
        if (!(value instanceof Date)) {
            return null;
        }
        status.setComplete(true);
        return new LocalDate((Date) value);
    }

    public Object deserializeProperty(Object propertyOwner, PropertyAccessor propertyAccessor, Object remoteValue, SerializationStatus status) {
        ensureIsNotDate(remoteValue, propertyAccessor.getPropertyName());
        if (!(remoteValue instanceof LocalDate)) {
            return null;
        }
        status.setComplete(true);
        LocalDate displayedDate = (LocalDate) remoteValue;
        return displayedDate.buildDate();
    }

    private void ensureIsNotDate(Object o, String name) {
        if (o instanceof Date) {
            throw new NonBusinessException("La arquitectura impone el uso de LocalDate por lo que Date no debe ser utilizado en el cliente para serializaci�n. " + (name == null ? "" : "Por favor mapee el campo '" + name + "' como LocalDate"));
        }
    }

    public Object deserializeParameter(Object source) {
        ensureIsNotDate(source, null);
        if (!(source instanceof LocalDate)) {
            return source;
        }
        LocalDate displayedDate = (LocalDate) source;
        return displayedDate.buildDate();
    }

    public Object serializeReturnValue(Object source) {
        if (!(source instanceof Date)) {
            return source;
        }
        return new LocalDate((Date) source);
    }
}
