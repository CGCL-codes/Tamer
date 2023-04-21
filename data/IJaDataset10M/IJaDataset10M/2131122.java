package com.google.gwt.dom.builder.client;

import com.google.gwt.dom.builder.shared.HtmlVideoBuilderTest;

/**
 * GWT tests for {@link DomVideoBuilder}.
 */
public class GwtVideoBuilderTest extends HtmlVideoBuilderTest {

    @Override
    public String getModuleName() {
        return GWT_MODULE_NAME;
    }
}
