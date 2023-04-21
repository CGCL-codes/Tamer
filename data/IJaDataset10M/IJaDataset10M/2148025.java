package org.genxdm.processor.w3c.xs.exception.sm;

import javax.xml.namespace.QName;
import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.exceptions.SchemaException;
import org.genxdm.xs.resolve.LocationInSchema;

/**
 * Validation Rule: Element Locally Valid (Complex Type)
 */
@SuppressWarnings("serial")
public abstract class SmComplexTypeException extends SmLocationException {

    public static final String PART_ABSTRACT_FALSE = "1";

    public static final String PART_CONTENT_TYPE_IS_EMPTY = "2.1";

    public static final String PART_CONTENT_TYPE_IS_SIMPLE = "2.2";

    public static final String PART_CONTENT_TYPE_ELEMENTONLY_AND_NON_WHITE_SPACE = "2.3";

    public static final String PART_CONTENT_TYPE_AND_CHILD_SEQUENCE = "2.4";

    public static final String PART_ATTRIBUTE_VALID = "3.1";

    public static final String PART_WILDCARD_ABSENT = "3.2.1";

    public static final String PART_WILDCARD_MATCH = "3.2.2";

    public static final String PART_ATTRIBUTE_REQUIRED_MISSING = "4";

    private final QName elementName;

    public final QName getElementName() {
        return elementName;
    }

    public SmComplexTypeException(final String partNumber, final QName elementName, final LocationInSchema elementLocation) {
        super(ValidationOutcome.CVC_Complex_Type, partNumber, elementLocation);
        this.elementName = PreCondition.assertArgumentNotNull(elementName, "elementName");
    }

    public SmComplexTypeException(final String partNumber, final QName elementName, final LocationInSchema elementLocation, final SchemaException cause) {
        super(ValidationOutcome.CVC_Complex_Type, partNumber, elementLocation, PreCondition.assertArgumentNotNull(cause, "cause"));
        this.elementName = PreCondition.assertArgumentNotNull(elementName, "elementName");
    }
}
