package org.imogene.web.gwt.client.ui.panel;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;

public class BottomBannerPanel extends Composite {

    private HorizontalPanel main;

    private Hyperlink legalLink;

    private Hyperlink copyrightsLink;

    private Hyperlink contactUsLink;

    public BottomBannerPanel() {
        layout();
    }

    private void layout() {
        main = new HorizontalPanel();
        legalLink = new Hyperlink(BaseNLS.constants().bottom_link_legal(), "legal");
        copyrightsLink = new Hyperlink(BaseNLS.constants().bottom_link_copyrights(), "copyrights");
        contactUsLink = new Hyperlink(BaseNLS.constants().bottom_link_contact(), "contactus");
        main.add(legalLink);
        main.add(new Label("|"));
        main.add(copyrightsLink);
        main.add(new Label("|"));
        main.add(contactUsLink);
        initWidget(main);
        properties();
    }

    private void properties() {
        main.setSpacing(5);
        setStylePrimaryName("imogene-BottomPanel");
    }
}
