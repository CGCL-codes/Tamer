package org.genxdm.processor.w3c.xs.exception.cvc;

import javax.xml.namespace.QName;
import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.resolve.LocationInSchema;
import org.genxdm.xs.types.SimpleType;

@SuppressWarnings("serial")
public final class CvcAttributeOnSimpleTypeException extends CvcAttributeException {

    private final QName elementName;

    private final SimpleType simpleType;

    public CvcAttributeOnSimpleTypeException(final QName elementName, final QName attributeName, final SimpleType simpleType, final LocationInSchema location) {
        super("?", attributeName, location);
        this.elementName = PreCondition.assertArgumentNotNull(elementName, "elementName");
        this.simpleType = PreCondition.assertArgumentNotNull(simpleType, "simpleType");
    }

    @Override
    public String getMessage() {
        return "The attribute '" + getAttributeName() + "' is not permitted on the element '" + elementName + "' because the element has a simple type definition, '" + simpleType + "'.";
    }
}
