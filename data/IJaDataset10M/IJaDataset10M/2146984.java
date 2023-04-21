package com.simoncat.beans;

import com.simoncat.vo.Server;
import java.util.Collection;

public class EventServerOutput {

    private String pageTitle;

    private Collection servers;

    public String getPageTitle() {
        return this.pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Collection getServers() {
        return this.servers;
    }

    public void setServers(Collection servers) {
        this.servers = servers;
    }

    private String formAction;

    private String formMethod;

    private Collection fields;

    private String enctype;

    public String getFormAction() {
        return this.formAction;
    }

    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    public String getFormMethod() {
        return this.formMethod;
    }

    public void setFormMethod(String formMethod) {
        this.formMethod = formMethod;
    }

    public Collection getFields() {
        return this.fields;
    }

    public void setFields(Collection fields) {
        this.fields = fields;
    }

    public String getEnctype() {
        return this.enctype;
    }

    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }
}
