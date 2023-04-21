package org.apache.axis2.jaxws.description.validator;

import org.apache.axis2.jaxws.description.OperationDescription;
import org.apache.axis2.jaxws.description.OperationDescriptionJava;
import org.apache.axis2.jaxws.description.OperationDescriptionWSDL;

/**
 * 
 */
public class OperationDescriptionValidator extends Validator {

    private OperationDescription opDesc;

    private OperationDescriptionJava opDescJava;

    private OperationDescriptionWSDL opDescWSDL;

    public OperationDescriptionValidator(OperationDescription toValidate) {
        opDesc = toValidate;
        opDescJava = (OperationDescriptionJava) opDesc;
        opDescWSDL = (OperationDescriptionWSDL) opDesc;
    }

    @Override
    public boolean validate() {
        if (getValidationLevel() == ValidationLevel.OFF) {
            return VALID;
        }
        return VALID;
    }
}
