package no.ugland.utransprod.model.validators;

import no.ugland.utransprod.gui.model.EmployeeModel;
import no.ugland.utransprod.util.ModelUtil;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;

/**
 * Validator for ansatt
 * @author atle.brekka
 */
public class EmployeeValidator implements Validator {

    /**
     * Holds the order to be validated.
     */
    private EmployeeModel employeeModel;

    /**
     * Constructs an OrderValidator on the given Order.
     * @param aEmployeeModel
     */
    public EmployeeValidator(final EmployeeModel aEmployeeModel) {
        this.employeeModel = aEmployeeModel;
    }

    /**
     * Validates this Validator's Order and returns the result as an instance of
     * {@link ValidationResult}.
     * @return the ValidationResult of the order validation
     */
    public final ValidationResult validate() {
        PropertyValidationSupport support = new PropertyValidationSupport(employeeModel, "Ansatt");
        if (ValidationUtils.isBlank(ModelUtil.nullToString(employeeModel.getFirstName()))) {
            support.addError("fornavn", "m� settes");
        }
        if (ValidationUtils.isBlank(ModelUtil.nullToString(employeeModel.getLastName()))) {
            support.addError("etternavn", "m� settes");
        }
        if (employeeModel.getEmployeeType() == null) {
            support.addWarning("type", "b�r registreres");
        }
        return support.getResult();
    }
}
