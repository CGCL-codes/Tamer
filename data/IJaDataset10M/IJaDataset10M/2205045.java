package com.tunelib.client.managed.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tunelib.client.scaffold.place.ProxyDetailsView;
import com.tunelib.client.scaffold.place.ProxyListView;
import com.tunelib.client.managed.request.ArtistProxy;

/**
 * Details view for ArtistProxy.
 */
public class ArtistDetailsView extends Composite implements ProxyDetailsView<ArtistProxy> {

    interface Binder extends UiBinder<HTMLPanel, ArtistDetailsView> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);

    private static ArtistDetailsView instance;

    ArtistProxy proxy;

    @UiField
    SpanElement id;

    @UiField
    SpanElement version;

    @UiField
    SpanElement name;

    @UiField
    SpanElement displayRenderer;

    @UiField
    HasClickHandlers edit;

    @UiField
    HasClickHandlers delete;

    private Delegate delegate;

    public static ArtistDetailsView instance() {
        if (instance == null) {
            instance = new ArtistDetailsView();
        }
        return instance;
    }

    public ArtistDetailsView() {
        initWidget(BINDER.createAndBindUi(this));
    }

    public Widget asWidget() {
        return this;
    }

    public boolean confirm(String msg) {
        return Window.confirm(msg);
    }

    public ArtistProxy getValue() {
        return proxy;
    }

    @UiHandler("delete")
    public void onDeleteClicked(ClickEvent e) {
        delegate.deleteClicked();
    }

    @UiHandler("edit")
    public void onEditClicked(ClickEvent e) {
        delegate.editClicked();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void setValue(ArtistProxy proxy) {
        this.proxy = proxy;
        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
        name.setInnerText(proxy.getName() == null ? "" : String.valueOf(proxy.getName()));
        displayRenderer.setInnerText(com.tunelib.client.managed.ui.ArtistProxyRenderer.instance().render(proxy));
    }
}
