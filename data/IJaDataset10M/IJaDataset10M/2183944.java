package de.mogwai.kias.forms.util;

import org.w3c.dom.Element;

/**
 * Helper class for the table component.
 * 
 * @author Mirko Sertic <mail@mirkosertic.de>
 */
public class TableColumn {

    private int index;

    private String text;

    private Element element;

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
