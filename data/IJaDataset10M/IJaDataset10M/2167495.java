package org.bims.bimswebaccess.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Class to provide a validator method to assure that a double is
 * between specified range.
 * @author Oscar Mora Perez
 */
public class DoubleRangeValidator implements Validator {

    /**
     * Default constructor.
     */
    public DoubleRangeValidator() {
    }

    /**
     * Validates a double, checking if it is between the specified range.
     * @param facesContext FacesContext instance to send messages
     * @param uIComponent component instance which contains double object
     * @param object double to validate
     * @throws javax.faces.validator.ValidatorException triggered if any
     * problem occurs
     */
    public final void validate(final FacesContext facesContext, final UIComponent uIComponent, final Object object) throws ValidatorException {
        try {
            double doubleToValidate = Double.parseDouble(object.toString());
            double maxValue = Double.parseDouble(uIComponent.getAttributes().get("maxValue").toString());
            double minValue = Double.parseDouble(uIComponent.getAttributes().get("minValue").toString());
            if (!((doubleToValidate < maxValue) && (doubleToValidate > minValue))) {
                FacesMessage message = new FacesMessage();
                message.setSummary("Double value is not in the range specified [" + minValue + ", " + maxValue + "]");
                message.setSeverity(FacesMessage.SEVERITY_FATAL);
                throw new ValidatorException(message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage();
            message.setSummary("Double value is not in the range specified");
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            throw new ValidatorException(message);
        }
    }
}
