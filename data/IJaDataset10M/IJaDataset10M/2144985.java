package org.sourceforge.writexml;

public final class Document {

    private final Tag rootTag;

    public Document(final Tag rootTag) {
        this.rootTag = rootTag;
    }

    public void writeTo(final JaxpXmlWriter jaxpXmlWriter) throws XmlWriterException {
        jaxpXmlWriter.writeStartDocument(new Dtd("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"));
        jaxpXmlWriter.write(rootTag);
        jaxpXmlWriter.writeEndDocument();
    }
}
