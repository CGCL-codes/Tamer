package br.net.woodstock.rockframework.web.faces.spring;

import java.io.Serializable;

class PageViewAttribute implements Serializable {

    private static final long serialVersionUID = 1734820659448540052L;

    private String[] views;

    private String name;

    private Object value;

    public PageViewAttribute() {
        super();
    }

    public PageViewAttribute(final String[] views, final String name, final Object value) {
        super();
        this.views = views;
        this.name = name;
        this.value = value;
    }

    public String[] getViews() {
        return this.views;
    }

    public void setViews(final String[] views) {
        this.views = views;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }
}
