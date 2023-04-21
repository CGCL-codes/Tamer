package com.google.gxp.compiler.ifexpand;

import com.google.gxp.compiler.alerts.ErrorAlert;
import com.google.gxp.compiler.alerts.SourcePosition;
import com.google.gxp.compiler.base.Node;

/**
 * {@link com.google.gxp.compiler.alerts.Alert} which indicates that as
 * {@code gxp:elif} was found after an {@code gxp:else} inside of a
 * {@code gxp:if} block.
 */
public class ElifAfterElseError extends ErrorAlert {

    public ElifAfterElseError(SourcePosition pos, String displayName) {
        super(pos, "Cannot have a " + displayName + " after an else in a if block.");
    }

    public ElifAfterElseError(Node node) {
        this(node.getSourcePosition(), node.getDisplayName());
    }
}
