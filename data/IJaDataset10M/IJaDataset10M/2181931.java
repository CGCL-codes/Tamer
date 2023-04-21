package com.google.gwt.uibinder.test.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Used by {@link IsRenderableIntegrationTest}.
 */
public class LegacyComposite extends Composite {

    interface Binder extends UiBinder<Widget, LegacyComposite> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);

    @UiField
    SpanElement span;

    public LegacyComposite() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget getWidget() {
        return super.getWidget();
    }
}
