package com.google.i18n.pseudolocalization.message.impl;

import com.google.i18n.pseudolocalization.message.MessageFragmentVisitor;
import com.google.i18n.pseudolocalization.message.Placeholder;
import com.google.i18n.pseudolocalization.message.VisitorContext;

/**
 * A base for {@link Placeholder} implementations that implements the basic
 * visitor API.
 */
public abstract class AbstractPlaceholder implements Placeholder {

    public void accept(VisitorContext ctx, MessageFragmentVisitor mfv) {
        mfv.visitPlaceholder(ctx, this);
    }

    public abstract String getTextRepresentation();
}
