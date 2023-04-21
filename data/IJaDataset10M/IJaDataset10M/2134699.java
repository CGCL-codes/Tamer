package org.jcrexplorer.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class validates NAME property values.
 * 
 * @todo Implement this!
 */
public class NamePropertyTypeValidator implements Validator {

    private Log logger = LogFactory.getLog(this.getClass());

    public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {
        logger.warn("Validator not yet implemented!");
    }
}
