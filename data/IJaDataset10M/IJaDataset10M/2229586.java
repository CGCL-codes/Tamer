package com.google.gwt.dom.builder.client;

import com.google.gwt.dom.builder.shared.OptGroupBuilder;
import com.google.gwt.dom.client.OptGroupElement;

/**
 * DOM-based implementation of {@link OptGroupBuilder}.
 */
public class DomOptGroupBuilder extends DomElementBuilderBase<OptGroupBuilder, OptGroupElement> implements OptGroupBuilder {

    DomOptGroupBuilder(DomBuilderImpl delegate) {
        super(delegate);
    }

    @Override
    public OptGroupBuilder disabled() {
        assertCanAddAttribute().setDisabled(true);
        return this;
    }

    @Override
    public OptGroupBuilder label(String label) {
        assertCanAddAttribute().setLabel(label);
        return this;
    }
}
