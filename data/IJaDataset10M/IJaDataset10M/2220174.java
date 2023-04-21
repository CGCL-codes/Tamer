package com.bazaaroid.client.web.gwt.partnermodule.client.view;

import com.bazaaroid.client.web.gwt.partnermodule.client.view.locations.impl.LocationsTabViewImpl;
import com.bazaaroid.client.web.gwt.partnermodule.client.view.partnerinfo.impl.PartnerInfoTabViewImpl;
import com.bazaaroid.client.web.gwt.partnermodule.client.view.products.impl.ProductsTabViewImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PartnerModuleViewImpl extends Composite implements PartnerModuleView {

    @UiTemplate("PartnerModuleView.ui.xml")
    interface PartnerModuleViewUiBinder extends UiBinder<Widget, PartnerModuleViewImpl> {
    }

    private static PartnerModuleViewUiBinder uiBinder = GWT.create(PartnerModuleViewUiBinder.class);

    private Presenter presenter;

    @UiField
    VerticalPanel partnerInfoTab;

    @UiField
    VerticalPanel productsTab;

    @UiField
    VerticalPanel locationsTab;

    public PartnerModuleViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        partnerInfoTab.add(new PartnerInfoTabViewImpl());
        productsTab.add(new ProductsTabViewImpl());
        locationsTab.add(new LocationsTabViewImpl());
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public Widget asWidget() {
        return this;
    }
}
