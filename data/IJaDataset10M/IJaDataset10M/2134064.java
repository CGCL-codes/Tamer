package org.jdna.bmt.web.client.ui.scan;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MissingImagePanel extends Composite {

    public MissingImagePanel() {
        VerticalPanel p = new VerticalPanel();
        Label l = new Label("No Image");
        p.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        p.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        p.add(l);
        initWidget(p);
        addStyleName("MissingImage");
    }
}
