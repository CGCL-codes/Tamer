package net.kodra.supereasy.authentication.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class RootWidget extends Composite {

    private static RootWidgetUiBinder uiBinder = GWT.create(RootWidgetUiBinder.class);

    interface RootWidgetUiBinder extends UiBinder<Widget, RootWidget> {
    }

    public RootWidget() {
        initWidget(uiBinder.createAndBindUi(this));
        this.ensureDebugId(this.getClass().getName());
    }
}
