package uk.ac.ed.ph.jqtiplus.node.expression.operator;

import uk.ac.ed.ph.jqtiplus.attribute.value.IdentifierMultipleAttribute;
import uk.ac.ed.ph.jqtiplus.control.ProcessingContext;
import uk.ac.ed.ph.jqtiplus.control.ValidationContext;
import uk.ac.ed.ph.jqtiplus.node.expression.AbstractExpression;
import uk.ac.ed.ph.jqtiplus.node.expression.Expression;
import uk.ac.ed.ph.jqtiplus.node.expression.ExpressionParent;
import uk.ac.ed.ph.jqtiplus.types.Identifier;
import uk.ac.ed.ph.jqtiplus.validation.AttributeValidationError;
import uk.ac.ed.ph.jqtiplus.validation.ValidationResult;
import uk.ac.ed.ph.jqtiplus.validation.ValidationWarning;
import uk.ac.ed.ph.jqtiplus.value.NullValue;
import uk.ac.ed.ph.jqtiplus.value.RecordValue;
import uk.ac.ed.ph.jqtiplus.value.SingleValue;
import uk.ac.ed.ph.jqtiplus.value.Value;
import java.util.ArrayList;
import java.util.List;

/**
 * The record operator takes 0 or more single sub-expressions of any base-type. The result is A container with record
 * cardinality containing the values of the sub-expressions.
 * <p>
 * All sub-expressions with NULL values are ignored. If no sub-expressions are given (or all are NULL)
 * then the result is NULL.
 * <p>
 * This operator is not in specification, but it is needed for testing and to allow implementation of other
 * expressions (for example fieldValue).
 *
 * @see uk.ac.ed.ph.jqtiplus.value.Cardinality
 * @see uk.ac.ed.ph.jqtiplus.value.BaseType
 * 
 * @author Jiri Kajaba
 */
public class RecordEx extends AbstractExpression {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "recordEx";

    /** Name of identifiers attribute in xml schema. */
    public static final String ATTR_IDENTIFIERS_NAME = "identifiers";

    /**
     * Constructs expression.
     *
     * @param parent parent of this expression
     */
    public RecordEx(ExpressionParent parent) {
        super(parent);
        getAttributes().add(new IdentifierMultipleAttribute(this, ATTR_IDENTIFIERS_NAME, null));
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    /**
     * Gets value of identifiers attribute.
     *
     * @return value of identifiers attribute
     */
    public List<Identifier> getIdentifiers() {
        return getAttributes().getIdentifierMultipleAttribute(ATTR_IDENTIFIERS_NAME).getValues();
    }

    /**
     * Gets child of this expression with given identifier or null.
     * Identifier is not part of child, but this expression.
     *
     * @param identifier identifier of child
     * @return Gets child of this expression with given name or null
     */
    public Expression getChild(String identifier) {
        int index = getIdentifiers().indexOf(identifier);
        if (index != -1 && index < getChildren().size()) return getChildren().get(index);
        return null;
    }

    @Override
    protected ValidationResult validateAttributes(ValidationContext context) {
        ValidationResult result = super.validateAttributes(context);
        if (getIdentifiers().size() != getChildren().size()) result.add(new AttributeValidationError(getAttributes().get(ATTR_IDENTIFIERS_NAME), "Invalid number of identifiers. Expected: " + getChildren().size() + ", but found: " + getIdentifiers().size() + "."));
        List<Identifier> identifiers = new ArrayList<Identifier>();
        for (Identifier identifier : getIdentifiers()) if (!identifiers.contains(identifier)) identifiers.add(identifier); else result.add(new AttributeValidationError(getAttributes().get(ATTR_IDENTIFIERS_NAME), "Duplicate identifier: " + identifier));
        return result;
    }

    @Override
    protected ValidationResult validateChildren(ValidationContext context) {
        ValidationResult result = super.validateChildren(context);
        if (getChildren().size() == 0) result.add(new ValidationWarning(this, "Container should contain some children."));
        return result;
    }

    @Override
    protected Value evaluateSelf(ProcessingContext context, int depth) {
        RecordValue container = new RecordValue();
        int index = 0;
        for (Expression subExpression : getChildren()) {
            Identifier identifier = getIdentifiers().get(index++);
            Value value = subExpression.getValue(context);
            if (!value.isNull()) container.add(identifier, (SingleValue) value);
        }
        if (container.isNull()) return NullValue.INSTANCE;
        return container;
    }
}
