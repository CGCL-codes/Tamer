package com.google.gwt.dom.builder.client;

import com.google.gwt.dom.builder.shared.OptionBuilder;
import com.google.gwt.dom.client.OptionElement;

/**
 * DOM-based implementation of {@link OptionBuilder}.
 */
public class DomOptionBuilder extends DomElementBuilderBase<OptionBuilder, OptionElement> implements OptionBuilder {

    DomOptionBuilder(DomBuilderImpl delegate) {
        super(delegate);
    }

    @Override
    public OptionBuilder defaultSelected() {
        assertCanAddAttribute().setDefaultSelected(true);
        return this;
    }

    @Override
    public OptionBuilder disabled() {
        assertCanAddAttribute().setDisabled(true);
        return this;
    }

    @Override
    public OptionBuilder label(String label) {
        assertCanAddAttribute().setLabel(label);
        return this;
    }

    @Override
    public OptionBuilder selected() {
        assertCanAddAttribute().setSelected(true);
        return this;
    }

    @Override
    public OptionBuilder value(String value) {
        assertCanAddAttribute().setValue(value);
        return this;
    }
}
