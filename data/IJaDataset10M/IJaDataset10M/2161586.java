package com.google.gxp.compiler.parser;

import com.google.gxp.compiler.alerts.ErrorAlert;
import com.google.gxp.compiler.alerts.SourcePosition;

/**
 * {@link ErrorAlert} which indicates that an undefined entity was found.
 */
public class UndefinedEntityError extends ErrorAlert {

    public UndefinedEntityError(SourcePosition pos, String name) {
        super(pos, "Undefined entity: &" + name + ";");
    }
}
