package org.vardb.util.morphia.resources.dao;

import com.google.code.morphia.annotations.Entity;

@Entity("links")
public class CLink extends CAbstractResource {

    protected String type = "GENERAL";

    protected String text = "";

    protected String href = "";

    protected String target = "_blank";

    protected String description = "";

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(final String href) {
        this.href = href;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(final String target) {
        this.target = target;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public CLink() {
    }

    public CLink(String identifier) {
        this.identifier = identifier;
    }

    public String getHtml() {
        StringBuilder buffer = new StringBuilder();
        return buffer.toString();
    }
}
