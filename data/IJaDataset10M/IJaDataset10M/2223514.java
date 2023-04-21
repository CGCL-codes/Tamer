package com.neolab.crm.client.fwk;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.neolab.crm.client.app.base.ImageFactory;

public class Dialog extends DialogBox {

    private VerticalPanel content;

    private HorizontalPanel captionPanel;

    private HorizontalPanel buttonBar;

    private Image x;

    private Label title;

    public Dialog() {
        this(null);
    }

    public Dialog(Widget w) {
        VerticalPanel container;
        container = new VerticalPanel();
        addStyleName("simpleDialog");
        content = new VerticalPanel();
        content.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        if (w != null) content.add(w);
        container.add(content);
        setWidget(container);
        captionPanel = new HorizontalPanel();
        captionPanel.addStyleName("dialog-Caption");
        title = new Label("Title");
        Element td = getCellElement(0, 1);
        DOM.removeChild(td, (Element) td.getFirstChildElement());
        DOM.appendChild(td, captionPanel.getElement());
        captionPanel.add(title);
        captionPanel.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
        captionPanel.setCellVerticalAlignment(title, HasVerticalAlignment.ALIGN_MIDDLE);
        x = ImageFactory.x();
        x.addStyleName("hand");
        HTML glue = new HTML("&nbsp;");
        captionPanel.add(glue);
        captionPanel.setCellWidth(glue, "100%");
        captionPanel.add(x);
        captionPanel.setCellHorizontalAlignment(x, HasHorizontalAlignment.ALIGN_RIGHT);
        captionPanel.setCellVerticalAlignment(x, HasVerticalAlignment.ALIGN_MIDDLE);
        setGlassEnabled(true);
        setAnimationEnabled(true);
        center();
        content.addStyleName("dialog-Content");
    }

    @Override
    public void setTitle(String title) {
        this.title = new Label(title);
    }

    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent event) {
        NativeEvent nativeEvent = event.getNativeEvent();
        if (!event.isCanceled() && (event.getTypeInt() == Event.ONCLICK) && isCloseEvent(nativeEvent)) {
            this.hide();
        }
        super.onPreviewNativeEvent(event);
    }

    private boolean isCloseEvent(NativeEvent event) {
        return event.getEventTarget().equals(x.getElement());
    }

    public void addWidget(Widget w) {
        content.add(w);
    }
}
