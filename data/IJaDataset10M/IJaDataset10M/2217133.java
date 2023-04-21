package uk.ac.ed.ph.jqtiplus.node.shared.declaration;

import uk.ac.ed.ph.jqtiplus.attribute.value.StringAttribute;
import uk.ac.ed.ph.jqtiplus.control.ValidationContext;
import uk.ac.ed.ph.jqtiplus.group.shared.FieldValueGroup;
import uk.ac.ed.ph.jqtiplus.node.AbstractObject;
import uk.ac.ed.ph.jqtiplus.node.shared.FieldValue;
import uk.ac.ed.ph.jqtiplus.node.shared.FieldValueParent;
import uk.ac.ed.ph.jqtiplus.node.shared.VariableDeclaration;
import uk.ac.ed.ph.jqtiplus.validation.ValidationError;
import uk.ac.ed.ph.jqtiplus.validation.ValidationResult;
import uk.ac.ed.ph.jqtiplus.value.BaseType;
import uk.ac.ed.ph.jqtiplus.value.Cardinality;
import uk.ac.ed.ph.jqtiplus.value.Value;
import java.util.List;

/**
 * Default value of variableDeclaration.
 *
 * Specification of defaultValue is not good. Instead of [1..*] fieldValues, defaultValue should contain 1 baseValue
 * or multiple or ordered or record expression. It would be much more powerful and consistent.
 * 
 * @author Jiri Kajaba
 */
public class DefaultValue extends AbstractObject implements FieldValueParent {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "defaultValue";

    /** Name of interpretation attribute in xml schema. */
    public static final String ATTR_INTERPRETATION_NAME = "interpretation";

    /** Default value of interpretation attribute. */
    public static final String ATTR_INTERPRETATION_DEFAULT_VALUE = null;

    /**
     * Creates object.
     *
     * @param parent parent of this object
     */
    public DefaultValue(VariableDeclaration parent) {
        super(parent);
        getAttributes().add(new StringAttribute(this, ATTR_INTERPRETATION_NAME, ATTR_INTERPRETATION_DEFAULT_VALUE));
        getNodeGroups().add(new FieldValueGroup(this, 1, null));
    }

    /**
     * Creates object with a given value.
     *
     * @param parent parent of this object
     * @param value value to use
     */
    public DefaultValue(VariableDeclaration parent, Value value) {
        this(parent);
        getFieldValues().addAll(FieldValue.getValues(this, value));
        evaluate();
    }

    @Override
    public VariableDeclaration getParent() {
        return (VariableDeclaration) super.getParent();
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    /**
     * Gets value of interpretation attribute.
     *
     * @return value of interpretation attribute
     * @see #setInterpretation
     */
    public String getInterpretation() {
        return getAttributes().getStringAttribute(ATTR_INTERPRETATION_NAME).getValue();
    }

    /**
     * Sets new value of interpretation attribute.
     *
     * @param interpretation new value of interpretation attribute
     * @see #getInterpretation
     */
    public void setInterpretation(String interpretation) {
        getAttributes().getStringAttribute(ATTR_INTERPRETATION_NAME).setValue(interpretation);
    }

    /**
     * Gets fieldValue children.
     *
     * @return fieldValue children
     */
    public List<FieldValue> getFieldValues() {
        return getNodeGroups().getFieldValueGroup().getFieldValues();
    }

    public Cardinality getCardinality() {
        return getParent().getCardinality();
    }

    public BaseType getBaseType() {
        return getParent().getBaseType();
    }

    @Override
    protected ValidationResult validateChildren(ValidationContext context) {
        ValidationResult result = super.validateChildren(context);
        Cardinality cardinality = getParent().getCardinality();
        if (cardinality != null) {
            if (cardinality.isSingle() && getFieldValues().size() > 1) result.add(new ValidationError(this, "Invalid values count. Expected: " + 1 + ". Found: " + getFieldValues().size()));
        }
        return result;
    }

    /**
     * Evaluates value of this defaultValue.
     *
     * @return evaluated value of this defaultValue
     */
    public Value evaluate() {
        return FieldValue.getValue(getCardinality(), getFieldValues());
    }
}
