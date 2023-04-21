package com.jmex.model.collada.schema;

public class gl_sampler1D extends fx_sampler1D_common {

    public gl_sampler1D(gl_sampler1D node) {
        super(node);
    }

    public gl_sampler1D(org.w3c.dom.Node node) {
        super(node);
    }

    public gl_sampler1D(org.w3c.dom.Document doc) {
        super(doc);
    }

    public gl_sampler1D(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
        super(doc, namespaceURI, prefix, name);
    }

    public void adjustPrefix() {
        super.adjustPrefix();
    }

    public void setXsiType() {
        org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
        el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "gl_sampler1D");
    }
}
