package org.codehaus.groovy.grails.web.binding;

import java.util.List;
import java.util.Map;

/**
 * A PropertyEditor that is able to edit structured properties (properties made up of multiple field values).
 * The #assemble method takes the required type and a map of field values and create an instance of the required type.
 *
 * @since 1.0.4
 * @author Graeme Rocher
 */
public interface StructuredPropertyEditor {

    /**
     * @return The required fields
     */
    public List getRequiredFields();

    /**
     * @return The optional fields
     */
    public List getOptionalFields();

    /**
     * Assemble and bind a property value from the specified fieldValues and the given type
     * @param type The type
     * @param fieldValues The field values
     * @return A bound property
     * @throws IllegalArgumentException Thrown in one of the field values is illegal
     */
    public Object assemble(Class type, Map fieldValues) throws IllegalArgumentException;
}
