package com.google.gwt.dom.builder.shared;

/**
 * Tests for {@link HtmlDivBuilder}.
 */
public class HtmlDivBuilderTest extends ElementBuilderTestBase<DivBuilder> {

    @Override
    protected DivBuilder createElementBuilder(ElementBuilderFactory factory) {
        return factory.createDivBuilder();
    }

    @Override
    protected void endElement(ElementBuilderBase<?> builder) {
        builder.endDiv();
    }

    @Override
    protected DivBuilder startElement(ElementBuilderBase<?> builder) {
        return builder.startDiv();
    }
}
