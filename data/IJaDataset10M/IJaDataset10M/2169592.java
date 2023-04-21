package org.qtitools.qti.node.item.response.processing;

import org.qtitools.qti.exception.QTIEvaluationException;
import org.qtitools.qti.node.XmlObject;
import org.qtitools.qti.node.outcome.declaration.LookupTable;
import org.qtitools.qti.node.outcome.declaration.OutcomeDeclaration;
import org.qtitools.qti.validation.ValidationError;
import org.qtitools.qti.validation.ValidationResult;
import org.qtitools.qti.validation.ValidationWarning;
import org.qtitools.qti.value.BaseType;
import org.qtitools.qti.value.Cardinality;
import org.qtitools.qti.value.Value;

/**
 * The setOutcomeValue rule sets the value of an outcome variable to the value obtained from the associated expression.
 * An outcome variable can be updated with reference to A previously assigned value, in other words, the outcome variable
 * being set may appear in the expression where it takes the value previously assigned to it.
 * <p>
 * Special care is required when using the numeric base-types because floating point values can not be assigned to integer
 * variables and vice-versa. The truncate, round, or integerToFloat operators must be used to achieve numeric type conversion.
 * 
 * @author Jonathon Hare
 */
public class SetOutcomeValue extends ProcessResponseValue {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "setOutcomeValue";

    /**
	 * Constructs rule.
	 *
	 * @param parent parent of this rule
	 */
    public SetOutcomeValue(XmlObject parent) {
        super(parent);
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    public Cardinality[] getRequiredCardinalities(int index) {
        if (getIdentifier() != null) {
            OutcomeDeclaration declaration = getParentItem().getOutcomeDeclaration(getIdentifier());
            if (declaration != null && declaration.getCardinality() != null) return new Cardinality[] { declaration.getCardinality() };
        }
        return Cardinality.values();
    }

    public BaseType[] getRequiredBaseTypes(int index) {
        if (getIdentifier() != null) {
            OutcomeDeclaration declaration = getParentItem().getOutcomeDeclaration(getIdentifier());
            if (declaration != null && declaration.getBaseType() != null) return new BaseType[] { declaration.getBaseType() };
        }
        return BaseType.values();
    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = super.validate();
        if (getIdentifier() != null) {
            if (getParentItem().getOutcomeDeclaration(getIdentifier()) == null) result.add(new ValidationError(this, "Cannot find " + OutcomeDeclaration.CLASS_TAG + ": " + getIdentifier()));
            OutcomeDeclaration declaration = getParentItem().getOutcomeDeclaration(getIdentifier());
            if (declaration != null && declaration.getLookupTable() != null) result.add(new ValidationWarning(this, "Never used " + LookupTable.DISPLAY_NAME + " in " + OutcomeDeclaration.CLASS_TAG + ": " + getIdentifier()));
        }
        return result;
    }

    @Override
    public void evaluate() {
        Value value = getExpression().evaluate();
        OutcomeDeclaration declaration = getParentItem().getOutcomeDeclaration(getIdentifier());
        if (declaration == null) throw new QTIEvaluationException("Cannot find " + OutcomeDeclaration.CLASS_TAG + ": " + getIdentifier());
        declaration.setValue(value);
    }
}
