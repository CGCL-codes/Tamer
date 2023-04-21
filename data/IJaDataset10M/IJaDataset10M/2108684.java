package org.qtitools.qti.node.content.variable;

import java.util.List;
import org.qtitools.qti.attribute.value.IdentifierAttribute;
import org.qtitools.qti.attribute.value.IntegerAttribute;
import org.qtitools.qti.attribute.value.StringAttribute;
import org.qtitools.qti.exception.QTIEvaluationException;
import org.qtitools.qti.node.XmlNode;
import org.qtitools.qti.node.XmlObject;
import org.qtitools.qti.node.content.BodyElement;
import org.qtitools.qti.node.content.basic.FlowStatic;
import org.qtitools.qti.node.content.basic.InlineStatic;
import org.qtitools.qti.node.outcome.declaration.OutcomeDeclaration;
import org.qtitools.qti.node.shared.VariableDeclaration;
import org.qtitools.qti.validation.ValidationError;
import org.qtitools.qti.validation.ValidationResult;
import org.qtitools.qti.value.Cardinality;
import org.qtitools.qti.value.SingleValue;
import org.qtitools.qti.value.Value;

/**
 * This is the only way how to show variables to actor.
 * 
 * @author Jonathon Hare
 */
public class PrintedVariable extends BodyElement implements FlowStatic, InlineStatic, TextOrVariable {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "printedVariable";

    /** Name of identifier attribute in xml schema. */
    public static final String ATTR_IDENTIFIER_NAME = "identifier";

    /** Name of format attribute in xml schema. */
    public static final String ATTR_FORMAT_NAME = "format";

    /** Default value of format attribute. */
    public static final String ATTR_FORMAT_DEFAULT_VALUE = null;

    /** Name of base attribute in xml schema. */
    public static final String ATTR_BASE_NAME = "base";

    /** Default value of base attribute. */
    public static final int ATTR_BASE_DEFAULT_VALUE = 10;

    /** Printed result of this block. */
    private SingleValue result;

    /**
	 * Constructs block.
	 *
	 * @param parent parent of this block.
	 */
    public PrintedVariable(XmlObject parent) {
        super(parent);
        getAttributes().add(new IdentifierAttribute(this, ATTR_IDENTIFIER_NAME));
        getAttributes().add(new StringAttribute(this, ATTR_FORMAT_NAME, ATTR_FORMAT_DEFAULT_VALUE));
        getAttributes().add(new IntegerAttribute(this, ATTR_BASE_NAME, ATTR_BASE_DEFAULT_VALUE));
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    /**
	 * Gets value of identifier attribute.
	 *
	 * @return value of identifier attribute
	 * @see #setIdentifier
	 */
    public String getIdentifier() {
        return getAttributes().getIdentifierAttribute(ATTR_IDENTIFIER_NAME).getValue();
    }

    /**
	 * Sets new value of identifier attribute.
	 *
	 * @param identifier new value of identifier attribute
	 * @see #getIdentifier
	 */
    public void setIdentifier(String identifier) {
        getAttributes().getIdentifierAttribute(ATTR_IDENTIFIER_NAME).setValue(identifier);
    }

    /**
	 * Gets value of format attribute.
	 *
	 * @return value of format attribute
	 * @see #setFormat
	 */
    public String getFormat() {
        return getAttributes().getStringAttribute(ATTR_FORMAT_NAME).getValue();
    }

    /**
	 * Sets new value of format attribute.
	 *
	 * @param format new value of format attribute
	 * @see #getFormat
	 */
    public void setFormat(String format) {
        getAttributes().getStringAttribute(ATTR_FORMAT_NAME).setValue(format);
    }

    /**
	 * Gets value of base attribute.
	 *
	 * @return value of base attribute
	 * @see #setBase
	 */
    public Integer getBase() {
        return getAttributes().getIntegerAttribute(ATTR_BASE_NAME).getValue();
    }

    /**
	 * Sets new value of base attribute.
	 *
	 * @param base new value of base attribute
	 * @see #getBase
	 */
    public void setBase(Integer base) {
        getAttributes().getIntegerAttribute(ATTR_BASE_NAME).setValue(base);
    }

    @Override
    public ValidationResult validateAttributes() {
        ValidationResult result = super.validateAttributes();
        if (getIdentifier() != null) {
            VariableDeclaration declaration = null;
            if (getParentTest() != null) declaration = getParentTest().getOutcomeDeclaration(getIdentifier()); else {
                declaration = getParentItem().getOutcomeDeclaration(getIdentifier());
                if (declaration == null) {
                    declaration = getParentItem().getTemplateDeclaration(getIdentifier());
                }
            }
            if (declaration == null) result.add(new ValidationError(this, "Cannot find " + OutcomeDeclaration.CLASS_TAG + ": " + getIdentifier()));
            if (declaration != null && declaration.getCardinality() != null && !(declaration.getCardinality().isSingle() || declaration.getCardinality().isRecord())) {
                result.add(new ValidationError(this, "Invalid cardinality. Expected: " + Cardinality.SINGLE + ", but found: " + declaration.getCardinality()));
            }
        }
        return result;
    }

    @Override
    public void evaluate() {
        result = null;
        String identifier = getIdentifier();
        Value value = null;
        if (getParentItem() != null) {
            value = getParentItem().getValue(identifier);
            if (value == null) {
                value = getParentItem().getTemplateValue(identifier);
            }
        } else {
            value = getParentTest().getOutcomeValue(identifier);
        }
        if (value == null) throw new QTIEvaluationException("Outcome variable doesn't exist: " + identifier);
        if (value.isNull()) return;
        if (!value.getCardinality().isSingle()) throw new QTIEvaluationException("Outcome variable is wrong cardinality: " + value.getCardinality() + " Expected: " + Cardinality.SINGLE);
        result = (SingleValue) value;
    }

    /**
	 * Gets printed result of this block.
	 *
	 * @return printed result of this block
	 */
    public SingleValue getResult() {
        return result;
    }

    @Override
    public List<? extends XmlNode> getChildren() {
        return null;
    }
}
