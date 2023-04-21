package no.ugland.utransprod.model.validators;

import no.ugland.utransprod.gui.model.AttributeModel;
import no.ugland.utransprod.util.ModelUtil;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;

/**
 * Validator for kunde
 * @author atle.brekka
 */
public class AttributeValidator implements Validator {

    /**
     * Holds the order to be validated.
     */
    private AttributeModel attributeModel;

    /**
     * Constructs an OrderValidator on the given Order.
     * @param aAttributeModel
     */
    public AttributeValidator(final AttributeModel aAttributeModel) {
        this.attributeModel = aAttributeModel;
    }

    /**
     * Validates this Validator's Order and returns the result as an instance of
     * {@link ValidationResult}.
     * @return the ValidationResult of the order validation
     */
    public final ValidationResult validate() {
        PropertyValidationSupport support = new PropertyValidationSupport(attributeModel, "Attributt");
        if (ValidationUtils.isBlank(ModelUtil.nullToString(attributeModel.getName()))) {
            support.addError("navn", "m� settes");
        }
        if (!attributeModel.getYesNo() && (!ValidationUtils.isBlank(attributeModel.getProdCatNo()) || !ValidationUtils.isBlank(attributeModel.getProdCatNo2()))) {
            support.addError("produktkategori", "kan bare settes dersom Ja/Nei er satt");
        }
        return support.getResult();
    }
}
