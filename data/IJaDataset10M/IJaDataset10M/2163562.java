package com.googlecode.yoohoo.xmppcore.protocol;

import com.googlecode.yoohoo.utils.Utils;

public class XmlLangText {

    private String xmlLang;

    private String namespace;

    private String text;

    public XmlLangText() {
    }

    public XmlLangText(String text) {
        this.text = text;
    }

    public XmlLangText(String text, String xmlLang) {
        this.text = text;
        this.xmlLang = xmlLang;
    }

    public String getXmlLang() {
        return xmlLang;
    }

    public void setXmlLang(String xmlLang) {
        this.xmlLang = xmlLang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public int hashCode() {
        return (namespace == null ? 0 : namespace.hashCode()) + (xmlLang == null ? 0 : xmlLang.hashCode()) + (text == null ? 0 : text.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XmlLangText) {
            XmlLangText other = (XmlLangText) obj;
            return Utils.equalsEvenNull(namespace, other.namespace) && Utils.equalsEvenNull(xmlLang, other.xmlLang) && Utils.equalsEvenNull(text, other.text);
        }
        return false;
    }
}
