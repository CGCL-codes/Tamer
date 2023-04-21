package com.google.gwt.dom.builder.shared;

import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * HTML-based implementation of {@link HeadBuilder}.
 */
public class HtmlHeadBuilder extends HtmlElementBuilderBase<HeadBuilder> implements HeadBuilder {

    HtmlHeadBuilder(HtmlBuilderImpl delegate) {
        super(delegate);
    }

    @Override
    public HeadBuilder html(SafeHtml html) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HeadBuilder text(String text) {
        throw new UnsupportedOperationException();
    }
}
