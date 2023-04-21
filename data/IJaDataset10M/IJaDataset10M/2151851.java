package com.ark.fix.model.fixml;

import com.ark.fix.model.*;

public class EncodedTextGroup extends FIXMLAggregate {

    private EncodedTextLen _EncodedTextLen;

    private EncodedText _EncodedText;

    public EncodedTextLen getEncodedTextLen() {
        return _EncodedTextLen;
    }

    public void setEncodedTextLen(EncodedTextLen obj) {
        _EncodedTextLen = obj;
    }

    public void initEncodedTextLen(Object obj) throws ModelException {
        if (_EncodedTextLen != null) throw new ModelException("Value has already been initialized for EncodedTextLen.");
        setEncodedTextLen((EncodedTextLen) obj);
    }

    public EncodedText getEncodedText() {
        return _EncodedText;
    }

    public void setEncodedText(EncodedText obj) {
        _EncodedText = obj;
    }

    public void initEncodedText(Object obj) throws ModelException {
        if (_EncodedText != null) throw new ModelException("Value has already been initialized for EncodedText.");
        setEncodedText((EncodedText) obj);
    }

    public String[] getProperties() {
        String[] properties = { "EncodedTextLen", "EncodedText" };
        return properties;
    }

    public String[] getRequiredProperties() {
        String[] properties = {};
        return properties;
    }

    public String toFIXMessage() {
        StringBuffer sb = new StringBuffer("");
        if (_EncodedTextLen != null) sb.append(_EncodedTextLen.toFIXMessage());
        if (_EncodedText != null) sb.append(_EncodedText.toFIXMessage());
        return sb.toString();
    }

    public String toFIXML(String ident) {
        StringBuffer sb = new StringBuffer("");
        sb.append(ident + "<EncodedTextGroup>\n");
        if (_EncodedTextLen != null) sb.append(_EncodedTextLen.toFIXML(ident + "\t") + "\n");
        if (_EncodedText != null) sb.append(_EncodedText.toFIXML(ident + "\t") + "\n");
        sb.append(ident + "</EncodedTextGroup>");
        return sb.toString();
    }
}
