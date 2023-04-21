package com.akjava.wiki.client.ecs;

import java.util.List;

public class A {

    private String rel;

    private String href;

    private List<Object> elements;

    public String getRel() {
        return rel;
    }

    public A setRel(String rel) {
        this.rel = rel;
        return this;
    }

    public String getHref() {
        return href;
    }

    public A setHref(String href) {
        this.href = href;
        return this;
    }

    public String toString() {
        String ret = "<a href='" + href + "' rel='" + rel + "'>";
        for (int i = 0; i < elements.size(); i++) {
            ret += elements.get(i).toString();
        }
        ret += "</a>";
        return ret;
    }

    public A addElement(Object object) {
        elements.add(object);
        return this;
    }
}
