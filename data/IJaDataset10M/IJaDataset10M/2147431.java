package com.google.gwt.user.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

/**
 * A widget that can contain arbitrary HTML.
 * 
 * <p>
 * If you only need a simple label (text, but not HTML), then the
 * {@link com.google.gwt.user.client.ui.Label} widget is more appropriate, as it
 * disallows the use of HTML, which can lead to potential security issues if not
 * used properly.
 * </p>
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-HTML { }</li>
 * </ul>
 * 
 * <p>
 * <h3>Example</h3> {@example com.google.gwt.examples.HTMLExample}
 * </p>
 */
public class HTML extends Label implements HasHTML {

    /**
   * Creates an empty HTML widget.
   */
    public HTML() {
        setElement(DOM.createDiv());
        sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
        setStyleName("gwt-HTML");
    }

    /**
   * Creates an HTML widget with the specified HTML contents.
   * 
   * @param html the new widget's HTML contents
   */
    public HTML(String html) {
        this();
        setHTML(html);
    }

    /**
   * Creates an HTML widget with the specified contents, optionally treating it
   * as HTML, and optionally disabling word wrapping.
   * 
   * @param html the widget's contents
   * @param wordWrap <code>false</code> to disable word wrapping
   */
    public HTML(String html, boolean wordWrap) {
        this(html);
        setWordWrap(wordWrap);
    }

    public String getHTML() {
        return DOM.getInnerHTML(getElement());
    }

    public void setHTML(String html) {
        DOM.setInnerHTML(getElement(), html);
    }
}
