package org.gwanted.gwt.sample.xdoclet.client;

import java.util.ArrayList;
import java.util.Map;
import org.gwanted.gwt.core.client.ui.AbstractComposite;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * @gwt.class visibility="public" package="org.gwanted.gwt.sample.xdoclet.client"
 * @gwt.runtime-code
 */
public class XDoclet extends AbstractComposite {

    private FlowPanel innerPanel;

    private Label label1;

    public XDoclet(final Element container, final Map params) {
        super(container, params);
        label1 = new Label("Initial message");
        innerPanel = new FlowPanel();
        innerPanel.add(label1);
        initWidget(innerPanel);
    }

    /** @gwt.runtime-code stackTrace="true" notifyException="true" activityLog="true"
   *  @gwt.method visibility="public"
   */
    public void setText(final String message) {
        label1.setText(message);
    }

    /** @gwt.runtime-code stackTrace="true" notifyException="true" activityLog="true"
   *  @gwt.method visibility="public"
   */
    public void setWordWrap(final boolean value) {
        ArrayList list = null;
        list.get(1);
    }
}
