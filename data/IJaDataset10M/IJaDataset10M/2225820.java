package net.woodstock.rockapi.itext.beans.impl;

import com.lowagie.text.Element;
import com.lowagie.text.Phrase;

public class ItextPhrase extends ItextObjectImpl {

    private static final long serialVersionUID = -1019639632153210263L;

    private String text;

    public ItextPhrase() {
        super();
    }

    public Element getObject() {
        Phrase phrase = new Phrase(this.text);
        return phrase;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
