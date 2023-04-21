package org.open18.validation;

import org.open18.lookup.StateLookup;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.faces.Validator;

/** 
 * <p>
 * Ensure that the entry is a valid state. Null and empty string is permitted.
 * </p>
 * <p>
 * Alternatively, you could define this in the faces-config.xml descriptor.
 * </p>
 * <pre>
 * &lt;validator>
 *   &lt;validator-id>org.open18.StateValidator&lt;/validator>
 *   &lt;validator-class>org.open18.validation.StateValidator&lt;/validator>
 * &lt;/validator></pre>
 * <p>
 * However, if you choose to use the faces-config.xml descriptor, this class
 * must be in the src/model classpath hierarchy because JSF cannot see classes
 * in the Seam hot redeployable classloader.
 * </p>
 * <p>
 * Note that the component name is used as the validator id if an id is not
 * specified on the @Validator annotation.
 * </p>
 */
@Name("org.open18.StateValidator")
@Validator
public class StateValidator implements javax.faces.validator.Validator {

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (value == null || "".equals(value)) {
            return;
        }
        if (!(value instanceof String)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Expecting a string value for state", "Expecting a string value for state"));
        }
        for (String abbr : StateLookup.abbreviations) {
            if (abbr.equals(value)) {
                return;
            }
        }
        throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid state: " + value, "Invalid state: " + value));
    }
}
