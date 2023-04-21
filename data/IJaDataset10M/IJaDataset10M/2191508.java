package org.armedbear.j;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public final class XmlTreeElement {

    private final String name;

    private final Attributes attributes;

    private final int lineNumber;

    private final int columnNumber;

    public XmlTreeElement(String name, Attributes attributes, int lineNumber, int columnNumber) {
        this.name = name;
        this.attributes = new AttributesImpl(attributes);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public final String getName() {
        return name;
    }

    public String toString() {
        return getStatusText();
    }

    public String getStatusText() {
        FastStringBuffer sb = new FastStringBuffer(name);
        for (int i = 0; i < attributes.getLength(); i++) appendNameAndValue(sb, attributes.getQName(i), attributes.getValue(i));
        return sb.toString();
    }

    private void appendNameAndValue(FastStringBuffer sb, String name, String value) {
        sb.append(' ');
        sb.append(name);
        sb.append("=");
        final char quoteChar = value.indexOf('"') < 0 ? '"' : '\'';
        sb.append(quoteChar);
        sb.append(value);
        sb.append(quoteChar);
    }

    public final int getLineNumber() {
        return lineNumber;
    }

    public final int getColumnNumber() {
        return columnNumber;
    }
}
