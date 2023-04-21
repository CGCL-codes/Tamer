package com.jspx.jtxml.impl;

import com.jspx.jtxml.TElement;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2006-12-28
 * Time: 21:47:53
 */
public abstract class TxElement implements TElement {

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    protected String elementName = "";

    protected String elementString = "";

    public String getElementString() {
        return elementString;
    }

    public void setElementString(String elementString) {
        this.elementString = elementString;
    }

    public void addElementString(String elementString) {
        this.elementString = this.elementString + elementString;
    }

    public void clear() {
        elementString = null;
    }

    public boolean equals(Object element) {
        TxElement e = (TxElement) element;
        return e.getElementName().equals(elementName) && e.getElementString().equals(elementString);
    }
}
