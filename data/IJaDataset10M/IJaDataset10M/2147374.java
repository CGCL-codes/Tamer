package com.jlect.swebing.renderers.gwt.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.jlect.swebing.renderers.client.LabelRenderer;

/**
 * Label renderer GWT based implementation
 *
 * @author Sergey Kozmin
 * @since 18.11.2007 13:53:44
 */
public class LabelRendererGWTImpl extends AbstractGWTRenderer implements LabelRenderer {

    private Label getLabelWidget() {
        return (Label) ((CompositeAttachEventProvider) getWidget()).getTargetWidget();
    }

    protected Widget provideWidget() {
        return new CompositeAttachEventProvider(new Label());
    }

    public void setText(String text) {
        getLabelWidget().setText(text);
    }
}
