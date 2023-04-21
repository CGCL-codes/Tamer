package owl.client.managed.ui;

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
import owl.client.managed.request.OWLUserProxy;
import owl.client.scaffold.place.ProxyDetailsView;
import owl.client.scaffold.place.ProxyListView;

public abstract class OWLUserDetailsView_Roo_Gwt extends Composite implements ProxyDetailsView<OWLUserProxy> {

    @UiField
    SpanElement id;

    @UiField
    SpanElement version;

    @UiField
    SpanElement userName;

    @UiField
    SpanElement email;

    @UiField
    SpanElement created;

    @UiField
    SpanElement modified;

    @UiField
    SpanElement collections;

    @UiField
    SpanElement test;

    OWLUserProxy proxy;

    @UiField
    SpanElement displayRenderer;

    public void setValue(OWLUserProxy proxy) {
        this.proxy = proxy;
        id.setInnerText(proxy.getId() == null ? "" : String.valueOf(proxy.getId()));
        version.setInnerText(proxy.getVersion() == null ? "" : String.valueOf(proxy.getVersion()));
        userName.setInnerText(proxy.getUserName() == null ? "" : String.valueOf(proxy.getUserName()));
        email.setInnerText(proxy.getEmail() == null ? "" : String.valueOf(proxy.getEmail()));
        created.setInnerText(proxy.getCreated() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(proxy.getCreated()));
        modified.setInnerText(proxy.getModified() == null ? "" : DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT).format(proxy.getModified()));
        collections.setInnerText(proxy.getCollections() == null ? "" : String.valueOf(proxy.getCollections()));
        test.setInnerText(proxy.getTest() == null ? "" : String.valueOf(proxy.getTest()));
        displayRenderer.setInnerText(owl.client.managed.ui.OWLUserProxyRenderer.instance().render(proxy));
    }
}
