package org.centricframework.ui;

import org.centricframework.core.ClientEvent;
import org.codehaus.jackson.annotate.JsonGetter;

@SuppressWarnings("serial")
public class Textbox extends InputElement {

    private String value;

    public Textbox() {
        super();
    }

    protected String type() {
        return "textbox";
    }

    protected String idPrefix() {
        return "tbx";
    }

    @JsonGetter
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        setProperty("value", value);
    }

    @Override
    public void onValueCommit(ClientEvent event) {
        this.value = event.getEventData();
    }
}
