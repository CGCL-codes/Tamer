package com.google.gxp.compiler.parser;

import com.google.gxp.compiler.alerts.ErrorAlert;
import com.google.gxp.compiler.alerts.SourcePosition;

/**
 * {@link ErrorAlert} which indicates that a namespace URI for an unknown
 * namespace was encountered.
 */
public class UnknownNamespaceError extends ErrorAlert {

    public UnknownNamespaceError(SourcePosition sourcePosition, String nsUri) {
        super(sourcePosition, String.format("Unknown namespace '%s'", nsUri));
    }
}
