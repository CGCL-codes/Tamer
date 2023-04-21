package com.meterware.httpunit.dom;

import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;

/**
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
public class HTMLElementImpl extends ElementImpl implements HTMLElement {

    public static final String UNSPECIFIED_ATTRIBUTE = null;

    ElementImpl create() {
        return new HTMLElementImpl();
    }

    public void click() {
        doClickAction();
    }

    public void doClickAction() {
    }

    public String getId() {
        return getAttributeWithNoDefault("id");
    }

    public void setId(String id) {
        setAttribute("id", id);
    }

    public String getTitle() {
        return getAttributeWithNoDefault("title");
    }

    public void setTitle(String title) {
        setAttribute("title", title);
    }

    public String getLang() {
        return getAttributeWithNoDefault("lang");
    }

    public void setLang(String lang) {
        setAttribute("lang", lang);
    }

    public String getDir() {
        return getAttributeWithNoDefault("dir");
    }

    public void setDir(String dir) {
        setAttribute("dir", dir);
    }

    public String getClassName() {
        return getAttributeWithNoDefault("class");
    }

    public void setClassName(String className) {
        setAttribute("class", className);
    }

    public NodeList getElementsByTagName(String name) {
        return super.getElementsByTagName(((HTMLDocumentImpl) getOwnerDocument()).toNodeCase(name));
    }

    protected final String getAttributeWithDefault(String attributeName, String defaultValue) {
        if (hasAttribute(attributeName)) return getAttribute(attributeName);
        return defaultValue;
    }

    protected final String getAttributeWithNoDefault(String attributeName) {
        if (hasAttribute(attributeName)) return getAttribute(attributeName);
        return UNSPECIFIED_ATTRIBUTE;
    }

    protected boolean getBooleanAttribute(String name) {
        Attr attr = getAttributeNode(name);
        return attr != null && !attr.getValue().equalsIgnoreCase("false");
    }

    protected int getIntegerAttribute(String name) {
        String value = getAttribute(name);
        return value.length() == 0 ? 0 : Integer.parseInt(value);
    }

    protected int getIntegerAttribute(String name, int defaultValue) {
        String value = getAttribute(name);
        return value.length() == 0 ? defaultValue : Integer.parseInt(value);
    }

    protected void setAttribute(String name, boolean disabled) {
        setAttribute(name, disabled ? "true" : "false");
    }

    protected void setAttribute(String name, int value) {
        setAttribute(name, Integer.toString(value));
    }

    HTMLDocumentImpl getHtmlDocument() {
        return (HTMLDocumentImpl) getOwnerDocument();
    }
}
