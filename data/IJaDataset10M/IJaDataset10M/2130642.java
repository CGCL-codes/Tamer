package org.socialresume.client.mvp.view;

import org.socialresume.client.mvp.presenter.HeaderPresenter.Display;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class HeaderDisplayImpl extends Composite implements Display {

    private ToggleButtonWrapper adminButtonWrapper;

    private PushButton contactPush;

    protected HorizontalPanel horizontalPanel;

    protected Label status;

    public HeaderDisplayImpl() {
        FlexTable container = new FlexTable();
        horizontalPanel = new HorizontalPanel();
        status = new Label("");
        status.getElement().setId("status");
        container.setWidget(0, 0, status);
        container.setWidget(0, 1, horizontalPanel);
        container.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
        container.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
        ToggleButton adminToggler = new ToggleButton("Admin");
        adminButtonWrapper = new ToggleButtonWrapper(adminToggler);
        contactPush = new PushButton("Contact");
        horizontalPanel.add(adminToggler);
        horizontalPanel.add(contactPush);
        initWidget(container);
        this.setStyleName("header");
    }

    @Override
    public HasText getStatusText() {
        return this.status;
    }

    @Override
    public HasClickHandlers getContactButton() {
        return contactPush;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public HasValue<Boolean> getAdmin() {
        return adminButtonWrapper;
    }
}
