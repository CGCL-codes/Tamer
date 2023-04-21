package org.sourceforge.writexml;

public final class Cdata extends UnaryMicrotype<char[]> implements TextContent {

    private Cdata(final char[] value) {
        super(value);
    }

    public static Cdata cdata(final String text) {
        return new Cdata(text.toCharArray());
    }

    public void writeTo(final XmlWriter xmlWriter) throws XmlWriterException {
        xmlWriter.writeCdata(value);
    }
}
