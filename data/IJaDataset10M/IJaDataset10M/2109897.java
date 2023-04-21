package com.google.gwt.dom.builder.shared;

/**
 * HTML-based implementation of {@link LegendBuilder}.
 */
public class HtmlLegendBuilder extends HtmlElementBuilderBase<LegendBuilder> implements LegendBuilder {

    HtmlLegendBuilder(HtmlBuilderImpl delegate) {
        super(delegate);
    }

    @Override
    public LegendBuilder accessKey(String accessKey) {
        return trustedAttribute("accessKey", accessKey);
    }
}
