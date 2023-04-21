package com.google.gxp.compiler.alerts.common;

import com.google.gxp.compiler.alerts.ErrorAlert;
import com.google.gxp.compiler.alerts.SourcePosition;
import com.google.gxp.compiler.base.Node;

/**
 * {@link com.google.gxp.compiler.alerts.Alert} which indicates that an
 * attribute's value is invalid.
 */
public class InvalidAttributeValueError extends ErrorAlert {

    public InvalidAttributeValueError(Node attr) {
        this(attr.getSourcePosition(), attr.getDisplayName());
    }

    public InvalidAttributeValueError(SourcePosition sourcePosition, String displayName) {
        super(sourcePosition, displayName + " value is invalid");
    }
}
