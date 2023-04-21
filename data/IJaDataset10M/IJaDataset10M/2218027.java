package com.directi.qwick.wicket.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class CustomTextField extends TextField {

    private static final int DEFAULT_SIZE = 40;

    int size = DEFAULT_SIZE;

    public CustomTextField(String id) {
        this(id, null, DEFAULT_SIZE);
    }

    public CustomTextField(String id, IModel model, int size) {
        super(id, model);
        this.size = size;
        setMarkupId(getId());
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.put("size", size);
        if (this.hasFeedbackMessage()) tag.getAttributes().put("class", "frm-field-error"); else tag.getAttributes().put("class", "frm-text");
    }
}
