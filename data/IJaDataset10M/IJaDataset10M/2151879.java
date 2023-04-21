package com.google.code.guidatv.client.ui;

import com.google.code.guidatv.model.Transmission;
import com.google.code.guidatv.client.ui.widget.imported.DisclosurePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TransmissionWidget extends Composite implements HasResizeHandlers {

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    DisclosurePanel panel;

    @UiField
    Label time;

    @UiField
    Label description;

    @UiField
    Image infoLink;

    @UiField
    Image googleLink;

    @UiField
    Image wikipediaLink;

    @UiField
    Image imdbLink;

    @UiField
    TransmissionWidgetStyle style;

    interface Binder extends UiBinder<Widget, TransmissionWidget> {
    }

    interface TransmissionWidgetStyle extends CssResource {

        String panel();

        String besides();

        String imagelink();
    }

    @UiConstructor
    public TransmissionWidget(final Transmission transmission) {
        initWidget(binder.createAndBindUi(this));
        panel.setHeaderText(transmission.getName());
        DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.HOUR24_MINUTE);
        time.setText(format.format(transmission.getStart()));
        description.setText(transmission.getDescription());
        if (transmission.getMainLink() != null) {
            infoLink.addStyleName(style.imagelink());
            infoLink.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    Window.open(transmission.getMainLink(), "_blank", null);
                }
            });
        } else {
            infoLink.setVisible(false);
        }
        googleLink.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.open(new UrlBuilder().setProtocol("http").setHost("www.google.com").setPath("/search").setParameter("q", transmission.getName().replaceAll(" ", "+")).buildString(), "_blank", null);
            }
        });
        wikipediaLink.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.open(new UrlBuilder().setProtocol("http").setHost("it.wikipedia.org").setPath("/w/index.php").setParameter("search", transmission.getName().replaceAll(" ", "+")).buildString(), "_blank", null);
            }
        });
        imdbLink.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                Window.open(new UrlBuilder().setProtocol("http").setHost("www.imdb.com").setPath("/find").setParameter("s", "tt").setParameter("q", transmission.getName().replaceAll(" ", "+")).buildString(), "_blank", null);
            }
        });
        panel.addResizeHandler(new ResizeHandler() {

            @Override
            public void onResize(ResizeEvent event) {
                ResizeEvent.fire(TransmissionWidget.this, getOffsetWidth(), getOffsetHeight());
            }
        });
    }

    @Override
    public HandlerRegistration addResizeHandler(ResizeHandler handler) {
        return addHandler(handler, ResizeEvent.getType());
    }
}
