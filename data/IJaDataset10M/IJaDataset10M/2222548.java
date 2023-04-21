package org.thechiselgroup.choosel.visualization_component.text.client;

import org.thechiselgroup.choosel.core.client.ui.CSS;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public class DefaultTextItemLabel implements TextItemLabel {

    private Element element;

    public DefaultTextItemLabel() {
        this.element = DOM.createDiv();
    }

    @Override
    public void addStyleName(String cssClass) {
        assert cssClass != null;
        element.addClassName(cssClass);
    }

    @Override
    public Element getElement() {
        return element;
    }

    @Override
    public String getText() {
        return element.getInnerText();
    }

    @Override
    public void registerHandler(EventListener eventListener) {
        DOM.sinkEvents(element, Event.MOUSEEVENTS | Event.ONCLICK);
        DOM.setEventListener(element, eventListener);
    }

    @Override
    public void removeStyleName(String cssClass) {
        assert cssClass != null;
        element.removeClassName(cssClass);
    }

    @Override
    public void setFontSize(String fontSize) {
        CSS.setFontSize(getElement(), fontSize);
    }

    @Override
    public void setText(String text) {
        assert text != null;
        element.setInnerText(text);
    }
}
