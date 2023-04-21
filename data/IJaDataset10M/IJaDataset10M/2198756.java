package com.cromoteca.meshcms.server.modules;

import com.cromoteca.meshcms.server.core.ServerModule;
import com.cromoteca.meshcms.server.toolbox.Strings;
import java.io.IOException;
import javax.servlet.ServletException;

public abstract class FormField extends ServerModule {

    private Form form;

    private String label;

    private String name;

    private String value;

    private boolean invalid;

    private boolean required;

    @Override
    public String run() throws IOException, ServletException {
        form = Form.get();
        form.getFields().add(this);
        if (form.isPost()) {
            onPost(form);
        }
        return null;
    }

    public Form getForm() {
        return form;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return Strings.isNullOrEmpty(name) ? getId() : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    protected abstract void onPost(Form form);
}
