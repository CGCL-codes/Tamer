package com.google.gxp.compiler.codegen;

import com.google.gxp.compiler.alerts.ErrorAlert;
import com.google.gxp.compiler.alerts.SourcePosition;
import com.google.gxp.compiler.base.NativeType;
import com.google.gxp.compiler.base.OutputLanguage;

/**
 * {@code ErrorAlert} which indicates that an illegal native type
 * was encountered.
 */
public class IllegalTypeError extends ErrorAlert {

    public IllegalTypeError(SourcePosition pos, String outputLanguage, String type) {
        super(pos, "Illegal " + outputLanguage + " type: " + type);
    }

    public IllegalTypeError(NativeType type, OutputLanguage outputLanguage) {
        this(type.getSourcePosition(), outputLanguage.getDisplay(), type.getNativeType(outputLanguage));
    }
}
