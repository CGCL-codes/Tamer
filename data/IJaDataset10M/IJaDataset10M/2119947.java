package clp.util.property;

import clp.metadata.PropertyMetadata;
import clp.util.ValidationException;

/**
 * This interface is used for formatting, converting,
 * and validating property values.
 *
 * @author  Darren Broemmer
 */
public interface PropertyHandler {

    public Object convertToStringFormat(Object value) throws PropertyException;

    public Object convertToObjectFormat(Object value) throws PropertyException;

    public String convertToDisplayFormat(Object value) throws PropertyException;

    public void validateProperty(PropertyMetadata propertyMetadata, Object value) throws ValidationException;
}
