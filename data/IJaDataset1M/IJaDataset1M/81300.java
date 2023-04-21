package org.genxdm.processor.w3c.xs.exception.sm;

import javax.xml.namespace.QName;
import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.resolve.LocationInSchema;

@SuppressWarnings("serial")
public final class SmDuplicateAttributeException extends SmLocationException {

    public SmDuplicateAttributeException(final QName name, final LocationInSchema location) {
        super(ValidationOutcome.TODO, "?", location);
    }
}
